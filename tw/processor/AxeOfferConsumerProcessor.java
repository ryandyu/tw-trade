package com.sumridge.tw.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.fix50.IOI;

import com.sumridge.tw.bean.SecurityStatus;
import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.IOIBuilder;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.offer.Offer;


@Component
public class AxeOfferConsumerProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(AxeOfferConsumerProcessor.class);

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
		//exchange.getIn().setBody(offer);
		 
        SecurityStatus securityStatus = new SecurityStatus();
        securityStatus.setSecurityId(cusip);
        
        securityStatus.setOfferType(Integer.parseInt(offer.getType()));  
        String benchmarkSecurityId;
        if (offer.getSpreadIssue() != null && offer.getSpreadIssue().getSecurityIds() != null )  {
        	benchmarkSecurityId = StringUtils.stripToEmpty(XmlJaxbUtils.getCusip(offer.getSpreadIssue().getSecurityIds()));
        	securityStatus.setBenchmarkSecurityId(benchmarkSecurityId);
		}
	
        this.twTrade.getSecurities().put(securityStatus.getSecurityId(), securityStatus);
        exchange.getIn().setBody(null);      
    
		LOG.debug("publish IOI to trade web from offerring update");
			
	}
}
