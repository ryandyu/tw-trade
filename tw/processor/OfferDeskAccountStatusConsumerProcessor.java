package com.sumridge.tw.processor;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.offer.Offer;
import com.sumridge.xml.jaxb.status.PlatformType;
import com.sumridge.xml.jaxb.status.RestrictionType;
import com.sumridge.xml.jaxb.status.TradeStatus;

@Component
public class OfferDeskAccountStatusConsumerProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(OfferDeskAccountStatusConsumerProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing com.sumridge.xml.jaxb.status.TradeStatus message.");
        
        TradeStatus tradeStatus = exchange.getIn().getBody(com.sumridge.xml.jaxb.status.TradeStatus.class);
        
        for (PlatformType platformType : tradeStatus.getPlatforms())
        {
            String platformTypeDesk = StringUtils.stripToEmpty(platformType.getDesk());
            if (platformTypeDesk.isEmpty())
                continue;
            this.twTrade.getDeskFilter().put(platformTypeDesk, BooleanUtils.toBoolean(platformType.isEnabled()));
        }
        
        for (RestrictionType restrictionType : tradeStatus.getRestrictions())
        {
            String restrictionTypeId = StringUtils.stripToEmpty(restrictionType.getId());
            if (restrictionTypeId.isEmpty())
                continue;
            this.twTrade.getAccountFilter().put(restrictionTypeId, BooleanUtils.toBoolean(restrictionType.isEnabled()));
        }
        
        List<Offer> offers = new LinkedList<com.sumridge.xml.jaxb.offer.Offer>();
        for (Offer offer : this.twTrade.getOffers().values())
        {
            String oldType = offer.getType();
            String cusip = StringUtils.stripToNull(XmlJaxbUtils.getCusip(offer.getSecurityIds()));
        
            offer.setType(BooleanUtils.toBoolean(this.twTrade.getDeskFilter().get(StringUtils.stripToNull(offer.getDeskId())))
                    && BooleanUtils.toBoolean(this.twTrade.getAccountFilter().get(StringUtils.stripToNull(offer.getAccountId())))
                    && !this.twTrade.getSecIdFilter().containsKey(cusip) ? "1" : "0");
            
            if (!offer.getType().equals(oldType))
                offers.add(offer);
        }
        
        exchange.getIn().setBody(offers);
    }

}
