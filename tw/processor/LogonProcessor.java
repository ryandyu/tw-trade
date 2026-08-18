package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.SecurityListRequestType;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.SecurityListRequestBuilder;

@Component
public class LogonProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(LogonProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix44.Logon message.");
        if (this.twTrade.getSecurities().isEmpty())
        {
            LOG.debug("Subscribing to quickfix.fix50.SecurityList message.");
            
            exchange.getIn().setBody(
                    new SecurityListRequestBuilder()
                    .securityReqId("SMRD-SEC")
                    .securityListRequestType(SecurityListRequestType.ALL_SECURITIES)
                    .build()
                    );
        }
        else
        {
            this.twTrade.setMassQuoteAckLatch(null);
            
            exchange.getIn().setBody(null);
        }
    }

}
