package com.sumridge.tw.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.ClOrdID;
import quickfix.field.ExecBroker;
import quickfix.field.MultiLegReportingType;
import quickfix.field.OrderID;
import quickfix.field.PartyRole;
import quickfix.field.PartySubIDType;
import quickfix.field.PriceType;
import quickfix.field.Product;
import quickfix.field.SecurityIDSource;
import quickfix.field.Side;
import quickfix.field.TrdRegTimestampType;
import quickfix.fix50.TradeCaptureReport;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.TradeCaptureBuilder;
import com.sumridge.tw.builder.TradeCaptureReportAckBuilder;

@Component
public class TradeCaptureReportProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeCaptureReportProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.TradeCaptureReport message.");
        
        TradeCaptureReport tradeCaptureReport = exchange.getIn().getBody(TradeCaptureReport.class);
        
        String cusip = tradeCaptureReport.getSecurityID().getValue();
        
        if (!tradeCaptureReport.getSecurityIDSource().getValue().equals(SecurityIDSource.CUSIP) && tradeCaptureReport.isSetNoSecurityAltID())
        {
            TradeCaptureReport.NoSecurityAltID secAltIdGrp = new TradeCaptureReport.NoSecurityAltID();
            for (int i = 1; i <= tradeCaptureReport.getNoSecurityAltID().getValue(); i++)
            {
                tradeCaptureReport.getGroup(i, secAltIdGrp);
                if (secAltIdGrp.getSecurityAltIDSource().getValue().equals(SecurityIDSource.CUSIP))
                    break;
            }
            cusip = secAltIdGrp.getSecurityAltID().getValue();
		}
        
		String oppTrader = "", traderInitials = "", brokerInitials = "";
		
        boolean isUseDealerAcronym = "AUCN".equals(tradeCaptureReport.isSetField(10031) ?  tradeCaptureReport.getString(10031) : "") 
                || 
                (tradeCaptureReport.isSetTradeReportType() ? tradeCaptureReport.getTradeReportType().getValue() : 0) == 108;
		
        if (tradeCaptureReport.isSetNoRootPartyIDs())
        {
            TradeCaptureReport.NoRootPartyIDs rootPartyIdsGroup = new TradeCaptureReport.NoRootPartyIDs();
            TradeCaptureReport.NoRootPartyIDs.NoRootPartySubIDs rootPartySubIdsGroup = new TradeCaptureReport.NoRootPartyIDs.NoRootPartySubIDs();
            
            boolean tbt = false;
            for (int i = 1; i <= tradeCaptureReport.getNoRootPartyIDs().getValue(); i++)
            {
                tradeCaptureReport.getGroup(i, rootPartyIdsGroup);
            
                Map<Integer, String> rootPartySubIds = new HashMap<Integer, String>();
                if (rootPartyIdsGroup.isSetNoRootPartySubIDs())
                    for (int j = 1; j <= rootPartyIdsGroup.getNoRootPartySubIDs().getValue(); j++)
                    {
                        rootPartyIdsGroup.getGroup(j, rootPartySubIdsGroup);
                        rootPartySubIds.put(rootPartySubIdsGroup.getRootPartySubIDType().getValue(), rootPartySubIdsGroup.getRootPartySubID().getValue());
                    }

                String rootPartyId = rootPartyIdsGroup.getRootPartyID().getValue();
                char rootPartyIdSource = rootPartyIdsGroup.isSetRootPartyIDSource() ? rootPartyIdsGroup.getRootPartyIDSource().getValue() : ' ';
				
                switch (rootPartyIdsGroup.getRootPartyRole().getValue())
                {
                    case PartyRole.CLIENT_ID:
                        if (!isUseDealerAcronym && rootPartyIdSource != 'N')
                            brokerInitials = rootPartyId.replaceAll("[\\s\\.\\&]", "");
                        
                        break;
                        
                    case PartyRole.ORDER_ORIGINATION_TRADER:
                        if (this.twTrade.getRootPartyIds().containsKey(rootPartyId))
                        {
                            brokerInitials = this.twTrade.getRootPartyIds().get(rootPartyId);
                            if(brokerInitials != null)
                            {
                                tbt = true;
                                LOG.info("TBT trade");
                            }
                        }
                        
                        if (rootPartySubIds.containsKey(PartySubIDType.PERSON))
                            oppTrader = rootPartySubIds.get(PartySubIDType.PERSON);
//                          oppTrader = rootPartySubIds.get(PartySubIDType.PERSON) + "{" + rootPartyId + "}";
                        
                        break;
                        
                    case PartyRole.EXECUTING_TRADER:
                        traderInitials = this.twTrade.getRootPartyIds().getOrDefault(rootPartyId, rootPartyId);
                        if (rootPartySubIds.containsKey(4012))
                            traderInitials = rootPartySubIds.get(4012);
                        if (rootPartySubIds.containsKey(4007))
                            traderInitials = rootPartySubIds.get(4007);
                        
                        break;
                        
                    case 1013:
                        if (isUseDealerAcronym && !tbt)
                            brokerInitials = this.twTrade.getRootPartyIds().getOrDefault(rootPartyId, rootPartyId);
                        
                        break;
                        
                    default:
                        break;
                }
            }
        }
        
        if (tradeCaptureReport.isSetField(ExecBroker.FIELD))
            brokerInitials = tradeCaptureReport.getString(ExecBroker.FIELD);
        
        TradeCaptureReport.NoSides sidesGroup = new TradeCaptureReport.NoSides();
        tradeCaptureReport.getGroup(1, sidesGroup);
        
        char side = sidesGroup.getSide().getValue();
        char buySell = '0';
        
        if (side == Side.BUY)
            buySell = 'S';
        else if (side == Side.SELL)
            buySell = 'B';
        
        if (!"Y".equals(System.getProperty("twi.d2d.sweep.book", "N")))
        {
            LOG.info(String.format("skip booking : %s %s %f@%f %s %s", 
                    new Character(buySell).toString(), 
                    cusip, tradeCaptureReport.getLastQty().getValue(),
                    tradeCaptureReport.getDouble(20115), 
                    traderInitials, brokerInitials));
            
            exchange.getIn().setBody(null);
            
            return;
        }
		
        String trailer1 = "", trailer2 = "";
        
        if (tradeCaptureReport.isSetField(6731))
            trailer1 = tradeCaptureReport.getString(6731);
        else if (sidesGroup.isSetField(OrderID.FIELD))
            trailer1 = sidesGroup.getString(OrderID.FIELD);
        
        if (sidesGroup.isSetField(ClOrdID.FIELD))
            trailer2 = String.format("%s-%d", sidesGroup.getString(ClOrdID.FIELD), tradeCaptureReport.getProduct().getValue());
        
        java.util.Date orderExecuteTms = new java.util.Date();
        if (tradeCaptureReport.isSetNoTrdRegTimestamps())
        {
            TradeCaptureReport.NoTrdRegTimestamps trdRegTmsGroup = new TradeCaptureReport.NoTrdRegTimestamps();
            for (int i = 1; i <= tradeCaptureReport.getNoTrdRegTimestamps().getValue(); i++)
            {
                tradeCaptureReport.getGroup(i, trdRegTmsGroup);
                if (TrdRegTimestampType.EXECUTION_TIME == trdRegTmsGroup.getTrdRegTimestampType().getValue())
                    break;
            }
            orderExecuteTms = trdRegTmsGroup.getTrdRegTimestamp().getValue();
        }
		
        DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyyyMMdd");
        java.util.Date settleDate = new java.util.Date();
        if (tradeCaptureReport.isSetSettlDate())
            settleDate = dtFormat.parseLocalDate(tradeCaptureReport.getSettlDate().getValue()).toDate();

        Double cover =  tradeCaptureReport.isSetField(20111) ? tradeCaptureReport.getDouble(20111) : null;
        if(cover != null && tradeCaptureReport.isSetField(PriceType.FIELD) && tradeCaptureReport.getPriceType().getValue() == PriceType.SPREAD)
            cover *= 100.0;
        
        // for TSY reporting
        boolean tsyLeg = (tradeCaptureReport.isSetProduct() && tradeCaptureReport.getProduct().getValue() == Product.GOVERNMENT)
                        &&
                         (tradeCaptureReport.isSetMultiLegReportingType() && tradeCaptureReport.getMultiLegReportingType().getValue() == MultiLegReportingType.INDIVIDUAL_LEG_OF_A_MULTI_LEG_SECURITY)
//                      &&
//                       (tradeCaptureReport.isSetField(10031)  && "AUCN".equals(tradeCaptureReport.getString(10031)))
                         ;
        
        exchange.getIn().setBody(
						new TradeCaptureBuilder()
						.cusip(cusip)
                        .netPrice(tradeCaptureReport.isSetField(20115) ? tradeCaptureReport.getDouble(20115) : 0)
                        .quantity((int) tradeCaptureReport.getLastQty().getValue())
                        .tradeDate(dtFormat.parseLocalDate(tradeCaptureReport.getTradeDate().getValue()).toDate())
						.settleDate(settleDate)
						.addDate(new java.util.Date())
						.refSource(this.twTrade.getPlatformId().toString())
						.refId(tradeCaptureReport.isSetTradeReportID() ? tradeCaptureReport.getTradeReportID().getValue() : "")
						.traderOffice(101)
						.traderAccount(0)
						.activityTms(tradeCaptureReport.getTransactTime().getValue())
						.buySell(new Character(buySell).toString())
						.trailer1(trailer1)
						.trailer2(trailer2)
						.orderExecuteTms(orderExecuteTms)
						.oppTrader(oppTrader)
						.traderInitials(traderInitials)
                        .execAts("TWDA".equals(tradeCaptureReport.isSetField(9010) ? tradeCaptureReport.getString(9010) : "") ? null : "NA")
                        .execVenue(tradeCaptureReport.isSetField(23070) && tradeCaptureReport.getBoolean(23070) ? "MTF" : null)
						.brokerInitials(StringUtils.substring(brokerInitials, 0, 30))
						.cover(cover != null ? String.valueOf(cover.doubleValue()) : null)
						.tradeModifier1(tsyLeg ? "S" :  null)
						.build()
						);
	}

    public void acknowledge(Exchange exchange) throws Exception
    {
        LOG.info("Acknowledging quickfix.fix50.TradeCaptureReport message.");
        
        TradeCaptureReport tradeCaptureReport = exchange.getIn().getBody(TradeCaptureReport.class);
        
		exchange.getIn().setBody(
		                        new TradeCaptureReportAckBuilder()
								.tradeReportId(tradeCaptureReport.isSetTradeReportID() ? tradeCaptureReport.getTradeReportID().getValue(): "")
								.build()
								);
	}
}
