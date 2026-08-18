package com.sumridge.tw.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.fix50.IOI;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.IOIBuilder;
import com.sumridge.tw.service.IOIBlockFirmService;
import com.sumridge.xml.jaxb.offer.Offer;;


@Component
public class SmrdMarketDataProcessor  implements Processor
{
	private static final Logger LOG = LoggerFactory.getLogger(SmrdMarketDataProcessor.class);

    @Autowired
    private TWTrade twTrade;
    
 //   @Autowired
 //   private IOIBlockFirmService query;

    
    private boolean readyPricing;
    
	@Override
	public void process(Exchange exchange) throws Exception {

	    		
		LOG.debug("Processing com.sumridge.xml.jaxb.offer.Offer market data message.");

	/*	if (readyPricing  != true) {
			exchange.getIn().setBody(null);
			return;
		}
      */  		
		Offer mktOffer = exchange.getIn().getBody(Offer.class);	  
	
		String  blockFirm = " ";
				
        IOI ioi = new IOIBuilder().build(mktOffer, twTrade, blockFirm);   
        exchange.getIn().setBody(ioi);
        
		LOG.info("publish IOI to trade web from market data update");
	
	}
	
	public void ready() {
		readyPricing = true;
	}	
}

