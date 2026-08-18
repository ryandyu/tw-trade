package com.sumridge.tw.processor;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.LegBenchmarkPrice;
import quickfix.field.LegPrice;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteRespType;
import quickfix.field.QuoteStatus;
import quickfix.field.Side;
import quickfix.field.TradeDate;
import quickfix.field.TradeLegRefID;
import quickfix.fix50.QuoteResponse;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.QuoteRequestBuilder;
import com.sumridge.tw.builder.QuoteStatusReportBuilder;
import com.sumridge.tw.builder.RequestLegTypeBuilder;
import com.sumridge.tw.builder.TradeRequestBuilder;
import com.sumridge.tw.service.QuoteRequestService;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.common.PriceType;
import com.sumridge.xml.jaxb.common.SecurityType;
import com.sumridge.xml.jaxb.common.YieldType;
import com.sumridge.xml.jaxb.order.QuoteRequest;
import com.sumridge.xml.jaxb.order.RequestLegType;
import com.sumridge.xml.jaxb.order.TradeRequest;

@Component
public class QuoteResponseProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteResponseProcessor.class);

    @Autowired
    private QuoteRequestService quoteRequestService;
    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.QuoteResponse message.");

        QuoteResponse quoteResponse = exchange.getIn().getBody(QuoteResponse.class);

        String quoteReqId = quoteResponse.isSetField(QuoteReqID.FIELD) ? quoteResponse.getString(QuoteReqID.FIELD) : "";

        List<RequestLegType> requestLegTypes = new LinkedList<RequestLegType>();
       
        if (quoteResponse.isSetNoLegs())
        {
            QuoteResponse.NoLegs group = new QuoteResponse.NoLegs();
			
            for (int i = 1; i <= quoteResponse.getNoLegs().getValue(); i++)
            {
                quoteResponse.getGroup(i, group);
                
                SecurityType securityType = XmlJaxbUtils.commonJof.createSecurityType();

                if (group.isSetLegSecurityID())
                    XmlJaxbUtils.addCusip(securityType, group.getLegSecurityID().getValue());

                PriceType priceType = XmlJaxbUtils.commonJof.createPriceType(), spotPrice = XmlJaxbUtils.commonJof.createPriceType();

                if (group.isSetLegPriceType() && group.isSetField(LegPrice.FIELD))
                {
                    priceType.setValue(group.getDouble(LegPrice.FIELD));

                    switch (group.getLegPriceType().getValue())
                    {
                        case quickfix.field.PriceType.PERCENTAGE:
                            priceType.setType(1);
                            break;

                        case quickfix.field.PriceType.SPREAD:
                            priceType.setType(2);
                            priceType.setValue(100 * priceType.getValue());
                            break;
                    }
				}
				
                if (group.isSetLegBenchmarkPrice())
                {
                    spotPrice.setType(1);
                    spotPrice.setValue(group.getDouble(LegBenchmarkPrice.FIELD));
                }
				
				YieldType spotYield = XmlJaxbUtils.commonJof.createYieldType();
				
				if (group.isSetField(22571))
					spotYield.setValue(group.getDouble(22571));
				
				RequestLegType requestLegType = new RequestLegTypeBuilder()
                        .platformLegId(group.isSetField(TradeLegRefID.FIELD) ? group.getString(TradeLegRefID.FIELD) : "1")
                        .securityType(securityType)
                        .priceType(priceType)
                        .spotPrice(spotPrice)
                        .spotYield(spotYield)
                        .buySellInd((group.isSetLegSide() && group.getLegSide().getValue() == Side.BUY) ? "B" : "S")
                        .quantity(group.isSetLegOrderQty() ? group.getLegOrderQty().getValue() : 0)
                        .settleDate(group.isSetLegSettlDate() ? DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(group.getLegSettlDate().getValue()).toDate() : new java.util.Date())
                        .build();
				
                if (group.isSetLegSecurityType() && group.getLegSecurityType().getValue().equals("CORP"))
                    requestLegTypes.add(0, requestLegType);
                else
                    requestLegTypes.add(requestLegType);
            }
        }
        else
        {
            SecurityType securityType = XmlJaxbUtils.commonJof.createSecurityType();

            if (quoteResponse.isSetSecurityID())
                XmlJaxbUtils.addCusip(securityType, quoteResponse.getSecurityID().getValue());

            PriceType priceType = XmlJaxbUtils.commonJof.createPriceType(), spotPrice = XmlJaxbUtils.commonJof.createPriceType();

            if (quoteResponse.isSetPriceType() && quoteResponse.isSetPrice())
            {
                priceType.setValue(quoteResponse.getPrice().getValue());

                switch (quoteResponse.getPriceType().getValue())
                {
                    case quickfix.field.PriceType.PERCENTAGE:
                        priceType.setType(1);
                        break;

                    case quickfix.field.PriceType.SPREAD:
                        priceType.setType(2);
                        priceType.setValue(100 * priceType.getValue());
                        break;
                }
            }

            if (quoteResponse.isSetBenchmarkPrice())
            {
                spotPrice.setType(1);
                spotPrice.setValue(quoteResponse.getBenchmarkPrice().getValue());
            }

            YieldType spotYield = XmlJaxbUtils.commonJof.createYieldType();

            if (quoteResponse.isSetField(22570))
                spotYield.setValue(quoteResponse.getDouble(22570));
			
			requestLegTypes.add(new RequestLegTypeBuilder()
                    .platformLegId("1")
                    .securityType(securityType)
                    .priceType(priceType)
                    .spotPrice(spotPrice)
                    .spotYield(spotYield)
                    .buySellInd((quoteResponse.isSetSide() && quoteResponse.getSide().getValue() == Side.BUY) ? "B" : "S")
                    .quantity(quoteResponse.isSetOrderQty() ? quoteResponse.getOrderQty().getValue() : 0)
                    .settleDate(quoteResponse.isSetSettlDate() ? DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(quoteResponse.getSettlDate().getValue()).toDate() : new java.util.Date())
                    .build()
                    );
		}
        
		this.twTrade.getCounteredQuotes().remove(quoteReqId);
		
        switch (quoteResponse.getQuoteRespType().getValue())
        {
            case QuoteRespType.HIT_LIFT:
                TradeRequest.Legs legs = XmlJaxbUtils.orderJof.createTradeRequestLegs();
                legs.getLegs().addAll(requestLegTypes);
                QuoteRequest qr = XmlJaxbUtils.orderJof.createQuoteRequest();
                String quoteId = "[N/A]";

                if (quoteResponse.isSetQuoteID())
                    quoteId = quoteResponse.getQuoteID().getValue();
                
                try
                {
                    qr = quoteRequestService.getQuoteRequest(!quoteId.equals("[N/A]") ? quoteId : quoteReqId);
                }
                catch (Exception e)
                {
                    LOG.error("Error retrieving QuoteRequest legs.");
                    qr.setLegs(XmlJaxbUtils.orderJof.createQuoteRequestLegs());
                }
                finally
                {
                    if (qr == null)
                    {
                        qr = XmlJaxbUtils.orderJof.createQuoteRequest();
                        qr.setLegs(XmlJaxbUtils.orderJof.createQuoteRequestLegs());
                    }
                }
			
                if (!qr.getLegs().getLegs().isEmpty())
                {
                    com.sumridge.xml.jaxb.order.RequestLegType leg = legs.getLegs().get(0), qLeg = qr.getLegs().getLegs().get(0);
                    leg.setSecurity(qLeg.getSecurity());
                    leg.setBenchmark(qLeg.getBenchmark());
                    leg.setSettleDate(qLeg.getSettleDate());
                }
			
                exchange.getIn()
                        .setBody(
                                new TradeRequestBuilder()
                                        .legs(legs)
                                        .type("inbound")
                                        .platformId(this.twTrade.getPlatformId())
                                        .platformRequestId(quoteReqId)
                                        .customerAccount(qr.getCustomerAccount())
                                        .salesPerson(qr.getSalesPerson())
                                        .reasonCode(0)
                                        .traderDesk("CORP")
                                        .tradeDate(quoteResponse.isSetField(TradeDate.FIELD) ? quoteResponse.getUtcDateOnly(TradeDate.FIELD) : new java.util.Date())
                                        .statusCode((quoteResponse.isSetField(20074) && quoteResponse.getBoolean(20074)) ? 0 : 10)
                                        .defaultAction(0)
                                        .defaultActionTms(quoteResponse.isSetField(20082) ? LocalDateTime.now().plusSeconds(quoteResponse.getInt(20082)).toDate() : null)
                                        .build()
                                        );
                
                break;
		
            case QuoteRespType.COUNTER:
                this.twTrade.getCounteredQuotes().put(quoteReqId, requestLegTypes.get(0).getPrice());
                LocalDateTime timeout = LocalDateTime.now().plusSeconds(quoteResponse.getInt(20079));
			
                //20322  CustCounterDealerTied
                boolean tied = quoteResponse.isSetField(20322) && quoteResponse.getBoolean(20322) && requestLegTypes.get(0).getPrice().getValue() < -999998;

                if(tied)
                    requestLegTypes.clear();
                
                exchange.getIn().setBody(
                        new QuoteRequestBuilder()
                            .legs(requestLegTypes)
							.type("inbound")
							.platformId(this.twTrade.getPlatformId())
							.platformRequestId(quoteReqId)
							.reasonCode(tied ? 4 : 0)
                            .tradeDate(quoteResponse.isSetField(TradeDate.FIELD) ? quoteResponse.getUtcDateOnly(TradeDate.FIELD) : new java.util.Date())
                            .statusCode(5)
                            .transactionCode(QuoteResponse.MSGTYPE)
                            .customerRemarks(quoteResponse.getQuoteRespID().getValue())
                            .replyByTms(timeout.toDate())
							.goodTillTms(timeout.toDate())
							.defaultAction(0)
                            .defaultActionTms(quoteResponse.isSetField(20082) ? LocalDateTime.now().plusSeconds(quoteResponse.getInt(20082)).toDate() : null)
                            .build()
                            );
                break;
		
            case 7:
//                LOG.info(StringUtils.concat("Cancelling RFQ: ", quoteReqId));
                LOG.info(StringUtils.join("Cancelling RFQ: ", quoteReqId));

                exchange.getIn().setBody(
                        new QuoteRequestBuilder()
                            .platformId(this.twTrade.getPlatformId())
							.platformRequestId(quoteReqId).reasonCode(0)
							.statusCode(2).build()
							);
			
                break;
		
            case 8:
//                LOG.info(StringUtils.concat("Timeout RFQ: ", quoteReqId));
                LOG.info(StringUtils.join("Timeout RFQ: ", quoteReqId));
			
                exchange.getIn().setBody(
                        new QuoteRequestBuilder()
							.platformId(this.twTrade.getPlatformId())
							.platformRequestId(quoteReqId).reasonCode(0)
							.statusCode(14).build()
							);
			
                break;
		
            default:
//                LOG.info(StringUtils.concat("Ignore other status RFQ: ", quoteReqId, " ", quoteResponse.getQuoteRespType()));
                LOG.info(StringUtils.join("Ignore other status RFQ: ", quoteReqId, " ", quoteResponse.getQuoteRespType()));

                exchange.getIn().setBody(null);
                
                break;
        }
    }

    public void ack(Exchange exchange) throws Exception
    {
        LOG.debug("Acknowledging quickfix.fix50.QuoteResponse message.");
        
        QuoteResponse quoteResponse = exchange.getIn().getBody(QuoteResponse.class);
		
        exchange.getIn().setBody(
				new QuoteStatusReportBuilder()
				        .quoteRespId(quoteResponse.getQuoteRespID().getValue())
                        .transactTime(new LocalDateTime(DateTimeZone.UTC).toDate())
                        .quoteStatus(QuoteStatus.ACCEPTED)
                        .symbol(quoteResponse.getSymbol().getValue())
                        .build()
                        );
	}

}
