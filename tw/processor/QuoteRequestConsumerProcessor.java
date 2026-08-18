package com.sumridge.tw.processor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.ExecType;
import quickfix.field.LegOrderQty;
import quickfix.field.LegPrice;
import quickfix.field.LegPriceType;
import quickfix.field.LegSide;
import quickfix.field.LegSymbol;
import quickfix.field.OrdStatus;
import quickfix.field.QuoteCancelType;
import quickfix.field.QuoteRequestRejectReason;
import quickfix.field.QuoteStatus;
import quickfix.field.SecurityIDSource;
import quickfix.field.Side;
import quickfix.field.TradeLegRefID;
import quickfix.fix50.ExecutionReport;
import quickfix.fix50.Quote;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.ExecutionReportBuilder;
import com.sumridge.tw.builder.QuoteBuilder;
import com.sumridge.tw.builder.QuoteCancelBuilder;
import com.sumridge.tw.builder.QuoteRequestBuilder;
import com.sumridge.tw.builder.QuoteRequestRejectBuilder;
import com.sumridge.tw.builder.QuoteStatusReportBuilder;
import com.sumridge.tw.service.QuoteRequestService;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.common.PriceType;
import com.sumridge.xml.jaxb.order.QuoteRequest;
import com.sumridge.xml.jaxb.order.RequestLegType;

@Component
public class QuoteRequestConsumerProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRequestConsumerProcessor.class);

    @Autowired
    private QuoteRequestService quoteRequestService;
    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing com.sumridge.xml.jaxb.order.QuoteRequest message.");
        
        QuoteRequest quoteRequest = exchange.getIn().getBody(QuoteRequest.class);

        if (quoteRequest.getLegs().getLegs().isEmpty())
        {
            exchange.getIn().setBody(null);
            return;
        }
		
        String platformRequestId = quoteRequest.getPlatformRequestId();
        quoteRequest.setTraderId(this.twTrade.getBookName(quoteRequest.getTraderAccount()));
        
        String ownerTraderId = this.twTrade.getOwnerTraderIds().getOrDefault(quoteRequest.getTraderId() != null ? quoteRequest.getTraderId() : "NA", "");
        java.util.Date transactTime = new LocalDateTime(DateTimeZone.UTC).toDate();
        
        RequestLegType requestLegType = quoteRequest.getLegs().getLegs().get(0);
        
        char side = "B".equals(requestLegType.getBuySellInd()) ? Side.BUY : Side.SELL;

        quickfix.fix50.Message message = null;

		PriceType priceType = requestLegType.getPrice();
//		if(priceType.getType() == null || priceType.getType() == 0)
//		    priceType.setType(1);
		
		final ExecutionReport executionReport = new ExecutionReportBuilder()
				.quoteRespId(quoteRequest.getCustomerRemarks())
				.orderId(platformRequestId)
				.clOrdId(platformRequestId)
				
//				.execId(StringUtils.concat(requestLegType.getBuySellInd(), transactTime.getTime()))
				.execId(StringUtils.join(requestLegType.getBuySellInd(), transactTime.getTime()))
				
				.transactTime(transactTime)
				.tradeDate(quoteRequest.getTradeDate())
				.execType(ExecType.TRADE)
				.ordStatus(OrdStatus.FILLED)
				.symbol("[N/A]")
				.securityId(XmlJaxbUtils.getCusip(requestLegType.getSecurity()))
				.securityIdSource(SecurityIDSource.CUSIP)
				.securityType(quoteRequest.getTraderDesk()).currency("USD")
				.side(side)
				.priceType(quickfix.field.PriceType.PERCENTAGE)
				.price(priceType.getValue())
				.orderQty(requestLegType.getQuantity())
				.ownerTraderId(ownerTraderId)
				.leavesQty(0).cumQty(0).avgPx(0)
				.build();
		
        if (quoteRequest.getStatusCode() == 4 && quoteRequest.getReasonCode() == 5)
			message = executionReport;
        else if (quoteRequest.getStatusCode() == 4 || quoteRequest.getStatusCode() == 15)
        {
            PriceType counterPriceType = this.twTrade.getCounteredQuotes().remove(platformRequestId);

            double price = priceType.getType() == 1 ? 
                    BigDecimal.valueOf(priceType.getValue()).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue() 
                    : 
                    BigDecimal.valueOf(priceType.getValue() / 100).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
            
            if (counterPriceType != null
                    && priceType.getType() == counterPriceType.getType()
                    && Math.abs(price
                            - (counterPriceType.getType() == 1 ? 
                                    BigDecimal.valueOf(counterPriceType.getValue()).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue() 
                                    : 
                                    BigDecimal.valueOf(counterPriceType.getValue() / 100).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue())) <= 0.00001)
			{    
				message = executionReport;
			}
			else if (quoteRequest.getLegs().getLegs().size() > 1) 
			{
                List<Quote.NoLegs> noLegs = new LinkedList<Quote.NoLegs>();
                for (RequestLegType leg : quoteRequest.getLegs().getLegs())
                {
                    Quote.NoLegs group = new Quote.NoLegs();
                    
                    group.set(new LegSymbol("[N/A]"));
                    group.setString(TradeLegRefID.FIELD, leg.getPlatformLegId());
                    group.set(new LegSide("B".equals(leg.getBuySellInd()) ? Side.BUY : Side.SELL));
                    group.set(new LegPriceType(leg.getPrice().getType() == 1 ? quickfix.field.PriceType.PERCENTAGE : quickfix.field.PriceType.SPREAD));
                    
                    group.setDouble(LegPrice.FIELD,
                            leg.getPrice().getType() == 1 ? 
                                    BigDecimal.valueOf(leg.getPrice().getValue()).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue()
                                    : 
                                    BigDecimal.valueOf(leg.getPrice().getValue() / 100).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());
                    
					group.set(new LegOrderQty(leg.getQuantity()));
					
					noLegs.add(group);
				}
                
				message = new QuoteBuilder().quoteReqId(platformRequestId)
						.quoteId(quoteRequest.getRequestId()).quoteType(211)
						.symbol("[N/A]").transactTime(transactTime)
						.ownerTraderId(ownerTraderId)
						.clientFreeText1(quoteRequest.getTraderRemarks())
						.noLegs(noLegs)
						.confCoriBook(quoteRequest.getTraderId())
						.confTrsyBook(quoteRequest.getTraderId())
						.build();
            }
            else
            {    
                message = new QuoteBuilder()
                        .quoteReqId(quoteRequest.getStatusCode() == 15 ? null : platformRequestId)
                        .quoteRespId(quoteRequest.getStatusCode() == 15 ? quoteRequest.getCustomerRemarks() : null)
						.quoteId(quoteRequest.getRequestId())
						.quoteType(211)
						.symbol("[N/A]")
						.transactTime(transactTime)
						.ownerTraderId(ownerTraderId)
						.clientFreeText1(quoteRequest.getTraderRemarks())
						.side(side)
                        .priceType(priceType.getType() == 1 ? quickfix.field.PriceType.PERCENTAGE : quickfix.field.PriceType.SPREAD)
						.price(price).orderQty(requestLegType.getQuantity())
						.confCoriBook(quoteRequest.getTraderId())
						.confTrsyBook(quoteRequest.getTraderId())
						.build();
            }
			
//            if (!"Y".equals(quoteRequest.getAsap()))
//            {
                try
                {
                    quoteRequestService.updateQuoteRequestStatus(
                            new QuoteRequestBuilder()
                            .platformId(quoteRequest.getPlatformId())
                            .platformRequestId(platformRequestId)
                            .reasonCode(0)
                            .statusCode(11)
                            .build()
                            );
                }
                catch (Exception e)
                {
                    LOG.error("Unable to update QuoteRequest.");
                    throw ObjectHelper.wrapRuntimeCamelException(e);
                }
//            }
        }
        else if (quoteRequest.getStatusCode() == 2)
        {
            if ("AJ".equals(quoteRequest.getTransactionCode()))
            {
                if (StringUtils.isNotBlank(quoteRequest.getCustomerRemarks()))
                {
                    message = new QuoteStatusReportBuilder()
                        .quoteRespId(quoteRequest.getCustomerRemarks())
                        .symbol("[N/A]")
                        .transactTime(new LocalDateTime(DateTimeZone.UTC).toDate())
                        .quoteStatus(QuoteStatus.REJECTED)
                        .ownerTraderId(ownerTraderId)
                        .build();
                }
                else
                {
                    message = new QuoteCancelBuilder()
                        .quoteReqId(platformRequestId)
                        .quoteId(quoteRequest.getRequestId())
                        .quoteCancelType(QuoteCancelType.CANCEL_QUOTE_SPECIFIED_IN_QUOTEID)
                        .transactTime(transactTime)
                        .ownerTraderId(ownerTraderId)
                        .build();
                }
            }
            else if (quoteRequest.getReasonCode() == 0)
            {
                message = new QuoteRequestRejectBuilder()
                    .quoteReqId(platformRequestId)
                    .quoteRequestRejectReason(QuoteRequestRejectReason.PASS)
                    .quoteId(platformRequestId)
                    .noRelatedSym(0)
                    .transactTime(transactTime)
                    .ownerTraderId(ownerTraderId)
                    .build();
            }
        }
        
		exchange.getIn().setBody(message);
	}

    public void createQuoteRequest(QuoteRequest quoteRequest)
    {
        try
        {
            this.quoteRequestService.createQuoteRequest(quoteRequest);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

    public void updateQuoteRequest(QuoteRequest quoteRequest)
    {
        LOG.debug("Calling web service for pop-up: " + quoteRequest.getPlatformRequestId());
        
        try
        {
            this.quoteRequestService.updateQuoteRequest(quoteRequest);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

    public void updateQuoteRequestStatus(QuoteRequest quoteRequest)
    {
        try
        {
            this.quoteRequestService.updateQuoteRequestStatus(quoteRequest);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

    public void updateQuoteRequestStatus(Integer platformId, String platformRequestId, Integer statusCode)
    {
        this.updateQuoteRequestStatus(new QuoteRequestBuilder()
                .platformId(platformId)
                .platformRequestId(platformRequestId)
                .reasonCode(0)
                .statusCode(statusCode)
                .build()
                );
    }

}
