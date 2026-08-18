package com.sumridge.tw.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumridge.xml.jaxb.XmlJaxbUtils;

public class TradeCaptureBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeCaptureBuilder.class);

    private String cusip;
    private Double netPrice;
    private Integer quantity;
    private java.util.Date tradeDate, settleDate, addDate;
    private String refSource, refId;
    private Integer traderOffice, traderAccount;
    private java.util.Date activityTms;
    private String buySell;
    private String trailer1, trailer2;
    private java.util.Date orderExecuteTms;
    private String oppTrader;
    private String traderInitials, brokerInitials;
    private String execAts, execVenue;
    private String tradeModifier1;
    private String cover;
    private Integer priceType;
    private String sourceId;

    public TradeCaptureBuilder cusip(String cusip)
    {
        this.cusip = cusip;
        return this;
    }

    public TradeCaptureBuilder netPrice(Double netPrice)
    {
        this.netPrice = netPrice;
        return this;
    }

    public TradeCaptureBuilder quantity(Integer quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public TradeCaptureBuilder tradeDate(java.util.Date tradeDate)
    {
        this.tradeDate = tradeDate;
        return this;
    }

    public TradeCaptureBuilder settleDate(java.util.Date settleDate)
    {
        this.settleDate = settleDate;
        return this;
    }

    public TradeCaptureBuilder addDate(java.util.Date addDate)
    {
        this.addDate = addDate;
        return this;
    }

    public TradeCaptureBuilder refSource(String refSource)
    {
        this.refSource = refSource;
        return this;
    }

    public TradeCaptureBuilder refId(String refId)
    {
        this.refId = refId;
        return this;
    }

    public TradeCaptureBuilder traderOffice(Integer traderOffice)
    {
        this.traderOffice = traderOffice;
        return this;
    }

    public TradeCaptureBuilder traderAccount(Integer traderAccount)
    {
        this.traderAccount = traderAccount;
        return this;
    }

    public TradeCaptureBuilder activityTms(java.util.Date activityTms)
    {
        this.activityTms = activityTms;
        return this;
    }

    public TradeCaptureBuilder buySell(String buySell)
    {
        this.buySell = buySell;
        return this;
    }

    public TradeCaptureBuilder trailer1(String trailer1)
    {
        this.trailer1 = trailer1;
        return this;
    }

    public TradeCaptureBuilder trailer2(String trailer2)
    {
        this.trailer2 = trailer2;
        return this;
    }

    public TradeCaptureBuilder orderExecuteTms(java.util.Date orderExecuteTms)
    {
        this.orderExecuteTms = orderExecuteTms;
        return this;
    }

    public TradeCaptureBuilder oppTrader(String oppTrader)
    {
        this.oppTrader = oppTrader;
        return this;
    }

    public TradeCaptureBuilder traderInitials(String traderInitials)
    {
        this.traderInitials = traderInitials;
        return this;
    }

    public TradeCaptureBuilder brokerInitials(String brokerInitials)
    {
        this.brokerInitials = brokerInitials;
        return this;
    }

    public TradeCaptureBuilder execAts(String execAts)
    {
        this.execAts = execAts;
        return this;
    }

    public TradeCaptureBuilder execVenue(String execVenue)
    {
        this.execVenue = execVenue;
        return this;
    }

    public TradeCaptureBuilder cover(String cover)
    {
        this.cover = cover;
        return this;
    }

    public TradeCaptureBuilder priceType(Integer priceType)
    {
        this.priceType = priceType;
        return this;
    }

    public TradeCaptureBuilder tradeModifier1(String tradeModifier1)
    {
        this.tradeModifier1 = tradeModifier1;
        return this;
    }

    public TradeCaptureBuilder sourceId(String sourceId)
    {
        this.sourceId = sourceId;
        return this;
    }

    public com.sumridge.xml.jaxb.trade.TradeCapture build()
    {
        LOG.debug("Creating com.sumridge.xml.jaxb.trade.TradeCapture object.");
        
        com.sumridge.xml.jaxb.trade.TradeCapture tradeCapture = XmlJaxbUtils.tradeJof.createTradeCapture();
        tradeCapture.setCusip(this.cusip);
        tradeCapture.setNetPrice(this.netPrice);
        tradeCapture.setQuantity(this.quantity);
        tradeCapture.setTradeDate(this.tradeDate);
        tradeCapture.setSettleDate(this.settleDate);
        tradeCapture.setAddDate(this.addDate);
        tradeCapture.setRefSource(this.refSource);
        tradeCapture.setRefId(this.refId);
        tradeCapture.setTraderOffice(this.traderOffice);
        tradeCapture.setTraderAccount(this.traderAccount);
        tradeCapture.setActivityTms(this.activityTms);
        tradeCapture.setBuySell(this.buySell);
        tradeCapture.setTrailer1(this.trailer1);
        tradeCapture.setTrailer2(this.trailer2);
        tradeCapture.setOrderExecuteTms(this.orderExecuteTms);
        tradeCapture.setOppTrader(this.oppTrader);
        tradeCapture.setTraderInitials(this.traderInitials);
        tradeCapture.setBrokerInitials(this.brokerInitials);
        tradeCapture.setExecAts(this.execAts);
        tradeCapture.setExecVenue(this.execVenue);
        
        if(this.cover != null)
        {
            tradeCapture.setCover(this.cover);
            tradeCapture.setPriceType(this.priceType);
        }
        
        tradeCapture.setTradeModifier1(this.tradeModifier1);
        tradeCapture.setSourceId("TWI");
        
        return tradeCapture;
    }

}
