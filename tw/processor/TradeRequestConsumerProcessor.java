package com.sumridge.tw.processor;

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
import quickfix.field.OrdStatus;
import quickfix.field.QuoteStatus;
import quickfix.field.SecurityIDSource;
import quickfix.field.Side;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.ExecutionReportBuilder;
import com.sumridge.tw.builder.QuoteStatusReportBuilder;
import com.sumridge.tw.service.TradeRequestService;
import com.sumridge.xml.jaxb.order.RequestLegType;
import com.sumridge.xml.jaxb.order.TradeRequest;

@Component
public class TradeRequestConsumerProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeRequestConsumerProcessor.class);

    @Autowired
    private TWTrade twTrade;
    @Autowired
    private TradeRequestService tradeRequestService;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing com.sumridge.xml.jaxb.order.TradeRequest message.");
        
        TradeRequest tradeRequest = exchange.getIn().getBody(TradeRequest.class);
        if (tradeRequest.getLegs().getLegs().isEmpty())
        {
            exchange.getIn().setBody(null);
            return;
        }
        
        String platformRequestId = tradeRequest.getPlatformRequestId();
        tradeRequest.setTraderId(this.twTrade.getBookName(tradeRequest.getTraderAccount()));
        
        java.util.Date transactTime = new LocalDateTime(DateTimeZone.UTC).toDate();
        
        quickfix.fix50.Message message;

        switch (tradeRequest.getStatusCode())
        {
            case 1:
                RequestLegType requestLegType = tradeRequest.getLegs().getLegs().get(0);
                message = new ExecutionReportBuilder()
                        .orderId(platformRequestId)
                        .clOrdId(platformRequestId)
                        
//                        .execId(StringUtils.concat(requestLegType.getBuySellInd(), transactTime.getTime()))
                        .execId(StringUtils.join(requestLegType.getBuySellInd(), transactTime.getTime()))
                        
                        .transactTime(transactTime)
                        .tradeDate(tradeRequest.getTradeDate())
                        .execType(ExecType.TRADE)
                        .ordStatus(OrdStatus.FILLED)
                        .symbol("[N/A]")
                        .securityId(com.sumridge.xml.jaxb.XmlJaxbUtils.getCusip(requestLegType.getSecurity()))
                        .securityIdSource(SecurityIDSource.CUSIP)
                        .securityType(tradeRequest.getTraderDesk())
                        .currency("USD")
                        .side("B".equals(requestLegType.getBuySellInd()) ? Side.BUY : Side.SELL)
                        .priceType(quickfix.field.PriceType.PERCENTAGE)
                        .price(requestLegType.getPrice().getValue())
                        .orderQty(requestLegType.getQuantity())
                        .ownerTraderId(this.twTrade.getOwnerTraderIds().getOrDefault(tradeRequest.getTraderId() != null ? tradeRequest.getTraderId() : "NA", ""))
                        .leavesQty(0)
                        .cumQty(0)
                        .avgPx(0)
                        .build();
			break;
			
            case 2:
                message = new QuoteStatusReportBuilder()
					    .quoteRespId(platformRequestId + "_TRDREQ")
					    .quoteId(platformRequestId)
					    .transactTime(transactTime)
					    .quoteStatus(QuoteStatus.REJECTED)
					    .symbol("[N/A]")
					    .build();
                break;
		
            default:
                message = null;
                break;
		}
		
		exchange.getIn().setBody(message);
	}

    public void createTradeRequest(TradeRequest tradeRequest)
    {
        LOG.debug("Calling web service for pop-up: " + tradeRequest.getPlatformRequestId());
        
        try
        {
            this.tradeRequestService.createTradeRequest(tradeRequest);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }
}
