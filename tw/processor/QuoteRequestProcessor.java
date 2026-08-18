package com.sumridge.tw.processor;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import quickfix.field.LegPrice;
import quickfix.field.LegPriceType;
import quickfix.field.ListID;
import quickfix.field.PartyRole;
import quickfix.field.PartySubIDType;
import quickfix.field.QuoteStatus;
import quickfix.field.SecurityIDSource;
import quickfix.field.Side;
import quickfix.field.TradeDate;
import quickfix.field.TradeLegRefID;
import quickfix.fix50.QuoteRequest;

import com.sumridge.tw.builder.QuoteRequestBuilder;
import com.sumridge.tw.builder.QuoteStatusReportBuilder;
import com.sumridge.tw.builder.RequestLegTypeBuilder;
import com.sumridge.tw.builder.TradeRequestBuilder;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.common.PriceType;
import com.sumridge.xml.jaxb.common.SecurityType;
import com.sumridge.xml.jaxb.common.YieldType;
import com.sumridge.xml.jaxb.order.RequestLegType;

@Component
public class QuoteRequestProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRequestProcessor.class);

    @Value("${platform_id}")
    private Integer platformId;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing QuoteRequest message.");
        
        QuoteRequest quoteRequest = exchange.getIn().getBody(QuoteRequest.class);
        
        QuoteRequest.NoRelatedSym group = new QuoteRequest.NoRelatedSym();
        quoteRequest.getGroup(1, group);
        
        List<RequestLegType> requestLegTypes = new LinkedList<RequestLegType>();
        if (group.isSetNoLegs())
        {
            QuoteRequest.NoRelatedSym.NoLegs legsGroup = new QuoteRequest.NoRelatedSym.NoLegs();
            for (int i = 1; i <= group.getNoLegs().getValue(); i++)
            {
                group.getGroup(i, legsGroup);
                
                SecurityType security = XmlJaxbUtils.commonJof.createSecurityType(), benchmark = null;
                if (legsGroup.isSetLegSecurityIDSource() && legsGroup.isSetLegSecurityID())
                {
                    switch (legsGroup.getLegSecurityIDSource().getValue())
                    {
                        case SecurityIDSource.CUSIP:
                            XmlJaxbUtils.addCusip(security, legsGroup.getLegSecurityID().getValue());
                            break;
                    }
                }
                
                if (legsGroup.isSetLegSecurityType() && legsGroup.getLegSecurityType().getValue().equals("CORP") && legsGroup.isSetField(20142))
                {
                    benchmark = new SecurityType();
                    XmlJaxbUtils.addCusip(benchmark, legsGroup.getString(20142));
                }
                
                PriceType priceType = XmlJaxbUtils.commonJof.createPriceType(), spotPrice = XmlJaxbUtils.commonJof.createPriceType();
                if (legsGroup.isSetField(LegPrice.FIELD) && legsGroup.getLegPrice().getValue() > -9999)
                    priceType.setValue(legsGroup.getLegPrice().getValue());
                
                if (legsGroup.isSetField(LegPriceType.FIELD))
                {
                    switch (legsGroup.getInt(LegPriceType.FIELD))
                    {
                        case quickfix.field.PriceType.PERCENTAGE:
                            priceType.setType(1);
                            break;
                
                        case quickfix.field.PriceType.SPREAD:
                            priceType.setType(2);
                            priceType.setValue(100 * priceType.getValue());
                            break;

//                        case 105:
//                            priceType.setType(4);
//                            priceType.setValue(100 * priceType.getValue());
//                            break;

                    }
                }
                
                if (legsGroup.isSetLegBenchmarkPrice())
                {
                    spotPrice.setType(1);
                    spotPrice.setValue(legsGroup.getLegBenchmarkPrice().getValue());
                }
                
                YieldType spotYield = XmlJaxbUtils.commonJof.createYieldType();
                
                if (legsGroup.isSetField(22571))
                    spotYield.setValue(legsGroup.getDouble(22571));

                RequestLegType requestLegType = new RequestLegTypeBuilder()
                        .platformLegId(legsGroup.isSetField(TradeLegRefID.FIELD) ? legsGroup.getString(TradeLegRefID.FIELD) : null)
                        .buySellInd(legsGroup.isSetLegSide() ? (legsGroup.getLegSide().getValue() == Side.BUY ? "B" : "S") : null)
                        .quantity(legsGroup.isSetLegOrderQty() ? legsGroup.getLegOrderQty().getValue() : 0)
                        .settleDate( legsGroup.isSetLegSettlDate() ? DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(legsGroup.getLegSettlDate().getValue()).toDate() : null)
                        .priceType(priceType)
						.spotPrice(spotPrice).spotYield(spotYield)
						.securityType(security).benchmark(benchmark)
						.build();
				
                if (legsGroup.getLegSecurityType().getValue().equals("CORP"))
					requestLegTypes.add(0, requestLegType);
				else
					requestLegTypes.add(requestLegType);
			}
        }
        else
        {
            SecurityType security = XmlJaxbUtils.commonJof.createSecurityType(), benchmark = XmlJaxbUtils.commonJof.createSecurityType();
            if (group.isSetSecurityID())
                XmlJaxbUtils.addCusip(security, group.getSecurityID().getValue());
            
            if (group.isSetBenchmarkSecurityID())
                XmlJaxbUtils.addCusip(benchmark, group.getBenchmarkSecurityID().getValue());
            
            PriceType priceType = XmlJaxbUtils.commonJof.createPriceType(), spotPrice = XmlJaxbUtils.commonJof.createPriceType();
            if (group.isSetPrice() && group.getPrice().getValue() > -9999)
                priceType.setValue(group.getPrice().getValue());
            
            if (group.isSetPriceType())
            {
                switch (group.getPriceType().getValue())
                {
                    case quickfix.field.PriceType.PERCENTAGE:
                        priceType.setType(1);
                        break;
                    case quickfix.field.PriceType.SPREAD:
                        priceType.setType(2);
                        priceType.setValue(100 * priceType.getValue());
                        break;
//                    case 105:
//                        priceType.setType(2);
//                        priceType.setValue(100 * priceType.getValue());
//                        break;
                }
            }
            
            if (group.isSetBenchmarkPrice())
            {
                spotPrice.setType(1);
                spotPrice.setValue(group.getBenchmarkPrice().getValue());
            }
            
            YieldType spotYield = XmlJaxbUtils.commonJof.createYieldType();
            
            if (group.isSetField(22570))
                spotYield.setValue(group.getDouble(22570));
            
			requestLegTypes.add(
					 new RequestLegTypeBuilder()
					.platformLegId("1")
                    .buySellInd(group.isSetSide() ? (group.getSide().getValue() == Side.BUY ? "B" : "S") : null)
                    .quantity(group.isSetOrderQty() ? group.getOrderQty().getValue() : 0)
                    .settleDate(group.isSetSettlDate() ? DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(group.getSettlDate().getValue()).toDate() : null)
					.priceType(priceType).spotPrice(spotPrice)
					.spotYield(spotYield).securityType(security)
					.benchmark(benchmark)
					.build()
					);
		}
        
		String customerAccount = null, platformTraderId = null, salesPerson = null, customerRank = null;
		
		QuoteRequest.NoRelatedSym.NoPartyIDs partyIdsGroup = new QuoteRequest.NoRelatedSym.NoPartyIDs();
		QuoteRequest.NoRelatedSym.NoPartyIDs.NoPartySubIDs partyIdsSubGroup = new QuoteRequest.NoRelatedSym.NoPartyIDs.NoPartySubIDs();
        for (int i = 1; i <= group.getNoPartyIDs().getValue(); i++)
        {
            group.getGroup(i, partyIdsGroup);
            switch (partyIdsGroup.getPartyRole().getValue())
            {
                case PartyRole.EXECUTING_FIRM:
                    customerAccount = partyIdsGroup.getPartyID().getValue();
                    break;
                    
                case PartyRole.CLIENT_ID:
                    platformTraderId = partyIdsGroup.getPartyID().getValue();
                    for (int j = 1; j <= partyIdsGroup.getNoPartySubIDs().getValue(); j++)
                    {
                        partyIdsGroup.getGroup(j, partyIdsSubGroup);
                        switch (partyIdsSubGroup.getPartySubIDType().getValue())
                        {
                            case PartySubIDType.PERSON:
                                salesPerson = partyIdsSubGroup.getPartySubID().getValue() + "{" + platformTraderId + "}";
                                break;
                                
                            case 4006:
                                customerRank = partyIdsSubGroup.getPartySubID().getValue();
                                break;
                        }
                    }
                    break;
                    
                default:
                    break;
            }
        }
        
        if (group.isSetField(20075) && group.getBoolean(20075))
        {
            int timeoutPeriod = group.getInt(20079);
            int quoteTimePeriod = group.isSetField(20081) ? group.getInt(20081) : timeoutPeriod;
            int dueinTimePeriod = group.isSetField(20090) ? group.getInt(20090) : timeoutPeriod;

            LocalDateTime timeout = LocalDateTime.now().plusSeconds(group.getInt(20079));
            LocalDateTime dueInTime = dueinTimePeriod == 0 ? LocalDateTime.now().plusSeconds(Math.min(quoteTimePeriod, 120)) : LocalDateTime.now().plusSeconds(dueinTimePeriod);

            exchange.getIn().setBody(
							    new QuoteRequestBuilder()
								.type("inbound")
								.tradeDate( group.isSetField(TradeDate.FIELD) ? DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(group.getString(TradeDate.FIELD)).toDate() : new java.util.Date())
								.customerAccount(customerAccount)
								.customerRank(customerRank)
								.salesPerson(salesPerson)
								.legs(requestLegTypes)
								.platformId(this.platformId)
								.platformRequestId(quoteRequest.getQuoteReqID().getValue())
								.platformTraderId(platformTraderId)
								.traderAccount("101-000000")
								.statusCode(0)
								.reasonCode(0)
								.asap(dueinTimePeriod == 0 ? "Y" : "N")
								.replyByTms(timeout.toDate())
								.receiveTms(new java.util.Date())
								.defaultAction(0)
								.defaultActionTms(timeout.toDate())
								.customerRemarks(quoteRequest.isSetText() ? quoteRequest.getText().getValue() : null)
								.availableActions(group.isSetField(20074) && group.getBoolean(20074) ? "RESPOND,PASS" : "NOACTION")
								.allowPartialFill(group.isSetField(20156) && group.getBoolean(20156) ? "Y" : "N")
								.goodTillTms(group.isSetField(20072) ? dueInTime.plusSeconds(group.getInt(20072)).toDate() : null)
								.firmTms(group.isSetField(20082) ? dueInTime.plusSeconds(group.getInt(20082)).toDate() : null)
								.listId(group.isSetField(ListID.FIELD) ? group.getString(ListID.FIELD) : null)
								.itemCount(group.isSetField(6847) ? group.getInt(6847) : 0)
								.venue(group.isSetField(23070) && group.getBoolean(23070) ? "MTF" : null)
								.build()
								);
		} 
        else
        {
			exchange.getIn().setBody(
							new TradeRequestBuilder()
							.legs(requestLegTypes)
							.type("inbound")
							.platformId(this.platformId)
                            .platformRequestId(quoteRequest.getQuoteReqID().getValue())
							.platformTraderId(platformTraderId)
							.reasonCode(0)
							.traderDesk("CORP")
                            .tradeDate(group.isSetField(TradeDate.FIELD) ? DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(group.getString(TradeDate.FIELD)).toDate() : new java.util.Date())
							.statusCode(20)
							.customerAccount(customerAccount)
							.salesPerson(salesPerson)
                            .allowPartialFill(group.isSetField(20156) ? group.getString(20156) : null)
							.defaultAction(0)
                            .defaultActionTms(group.isSetField(20079) ? LocalDateTime.now().plusSeconds(group.getInt(20079)).toDate() : null)
                            .venue(group.isSetField(23070) && group.getBoolean(23070) ? "MTF" : null)
                            .build()
                            );
        }
	}

    public void ack(Exchange exchange) throws Exception
    {
        LOG.debug("Acknowledging QuoteRequest message.");
        
        QuoteRequest quoteRequest = exchange.getIn().getBody(QuoteRequest.class);
        QuoteRequest.NoRelatedSym group = new QuoteRequest.NoRelatedSym();
        quoteRequest.getGroup(1, group);
        
		exchange.getIn().setBody(
				new QuoteStatusReportBuilder()
				.quoteReqId(quoteRequest.getQuoteReqID().getValue())
				.quoteId(quoteRequest.getQuoteReqID().getValue())
                .transactTime(new LocalDateTime(DateTimeZone.UTC).toDate())
				.quoteStatus(QuoteStatus.ACCEPTED)
				.symbol(group.getSymbol().getValue())
				.build()
				);
	}

}
