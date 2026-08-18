package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.xml.jaxb.filter.OfferFilter;
import com.sumridge.xml.jaxb.filter.PlatformFilterType;
import com.sumridge.xml.jaxb.offer.Offer;

@Component
public class OfferSecIdFilterStatusConsumerProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(OfferSecIdFilterStatusConsumerProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing com.sumridge.xml.jaxb.filter.OfferFilter message.");
       
        OfferFilter offerFilter = exchange.getIn().getBody(OfferFilter.class);
        
        String secId = StringUtils.stripToEmpty(offerFilter.getSecurityId().getValue());
        if (secId.isEmpty())
        {
            exchange.getIn().setBody(null);
            return;
        }
        
        this.twTrade.getSecIdFilter().remove(secId);
        
        boolean enabled = offerFilter.isEnabled();
        if (enabled)
        {
            for (PlatformFilterType platformFilterType : offerFilter.getPlatformFilters())
            {
                if (this.twTrade.getPlatformId() != platformFilterType.getPlatformId())
                    continue;
                enabled = platformFilterType.isEnabled();
                break;
            }
        }
        
        if (!enabled)
            this.twTrade.getSecIdFilter().put(secId, enabled);
        
        Offer offer = this.twTrade.getOffers().get(secId);
        if (offer != null)
            offer.setType(BooleanUtils.toBoolean(this.twTrade.getDeskFilter().get(StringUtils.stripToNull(offer.getDeskId())))
                    && BooleanUtils.toBoolean(this.twTrade.getAccountFilter().get(StringUtils.stripToNull(offer.getAccountId())))
                    && !this.twTrade.getSecIdFilter().containsKey(secId) ? "1" : "0");
        
        exchange.getIn().setBody(offer);
    }
}
