package com.sumridge.tw.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.common.PriceType;
import com.sumridge.xml.jaxb.common.SecurityType;
import com.sumridge.xml.jaxb.common.YieldType;
import com.sumridge.xml.jaxb.order.RequestLegType;

public class RequestLegTypeBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(RequestLegTypeBuilder.class);

    private String platformLegId;
    private String buySellInd;
    private double quantity;
    private Double targetQty;
    private java.util.Date settleDate;
    private PriceType priceType, spotPrice;
    private SecurityType securityType, benchmark;
    private YieldType spotYield;

    public RequestLegTypeBuilder platformLegId(String platformLegId)
    {
        this.platformLegId = platformLegId;
        return this;
    }

    public RequestLegTypeBuilder buySellInd(String buySellInd)
    {
        this.buySellInd = buySellInd;
        return this;
    }

    public RequestLegTypeBuilder quantity(double quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public RequestLegTypeBuilder targetQty(double targetQty)
    {
        this.targetQty = targetQty;
        return this;
    }

    public RequestLegTypeBuilder settleDate(java.util.Date settleDate)
    {
        this.settleDate = settleDate;
        return this;
    }

    public RequestLegTypeBuilder priceType(PriceType priceType)
    {
        this.priceType = priceType;
        return this;
    }

    public RequestLegTypeBuilder spotPrice(PriceType spotPrice)
    {
        this.spotPrice = spotPrice;
        return this;
    }

    public RequestLegTypeBuilder securityType(SecurityType securityType)
    {
        this.securityType = securityType;
        return this;
    }

    public RequestLegTypeBuilder benchmark(SecurityType benchmark)
    {
        this.benchmark = benchmark;
        return this;
    }

    public RequestLegTypeBuilder spotYield(YieldType spotYield)
    {
        this.spotYield = spotYield;
        return this;
    }

    public RequestLegType build()
    {
        LOG.debug("Creating com.sumridge.xml.jaxb.order.RequestLegType object.");
        
        RequestLegType requestLegType = XmlJaxbUtils.orderJof.createRequestLegType();
        requestLegType.setPlatformLegId(this.platformLegId);
        requestLegType.setBuySellInd(this.buySellInd);
        requestLegType.setQuantity(this.quantity);
        requestLegType.setTargetQty(this.targetQty);
        requestLegType.setSettleDate(this.settleDate);
        requestLegType.setPrice(this.priceType);
        requestLegType.setSpotPrice(this.spotPrice);
        requestLegType.setSpotYield(this.spotYield);
        requestLegType.setSecurity(this.securityType);
        
        if (this.priceType.getType() != null 
                && (this.priceType.getType() == 2 || this.priceType.getType() == 4 || this.priceType.getType() == 3))
            requestLegType.setBenchmark(this.benchmark);
        
        return requestLegType;
    }
}
