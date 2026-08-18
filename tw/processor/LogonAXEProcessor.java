package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.bean.TWTrade;

@Component
public class LogonAXEProcessor  implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(LogonAXEProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
    	// pass thru processor - reserve for future hook
        LOG.debug("Processing quickfix.fix44.Logon message.");
        if (this.twTrade.getSecurities().isEmpty())
        {
        		
            exchange.getIn().setBody(null);
        }
        else
        {
            this.twTrade.setMassQuoteAckLatch(null);
            
            exchange.getIn().setBody(null);
        }
    }
    
}