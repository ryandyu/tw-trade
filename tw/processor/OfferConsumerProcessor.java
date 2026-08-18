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
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.offer.Offer;


@Component
public class OfferConsumerProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(OfferConsumerProcessor.class);

	@Autowired
	private TWTrade twTrade;
    
    
	@Override
	public void process(Exchange exchange) throws Exception 
	{

		LOG.debug("Processing com.sumridge.xml.jaxb.offer.Offer message.");
		
		Offer offer = exchange.getIn().getBody(Offer.class);
		
		if (offer == null || !"CORP".equals(offer.getDeskId())) 
		{
			exchange.getIn().setBody(null);
			return;
		}
		
		String cusip = XmlJaxbUtils.getCusip(offer.getSecurityIds());
		
        offer.setType(BooleanUtils.toBoolean(this.twTrade.getDeskFilter().get(StringUtils.stripToNull(offer.getDeskId())))
                && BooleanUtils.toBoolean(this.twTrade.getAccountFilter().get(StringUtils.stripToNull(offer.getAccountId())))
                && !this.twTrade.getSecIdFilter().containsKey(cusip) ? "1" : "0");
		
		this.twTrade.getOffers().put(cusip, offer);
		exchange.getIn().setBody(offer);
		 
 	}
}
