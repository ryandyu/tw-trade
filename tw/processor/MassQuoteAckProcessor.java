package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.fix50.MassQuoteAcknowledgement;

import com.sumridge.tw.bean.TWTrade;

@Component
public class MassQuoteAckProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(MassQuoteAckProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        MassQuoteAcknowledgement massQuoteAck = exchange.getIn().getBody(MassQuoteAcknowledgement.class);
       
        LOG.debug("Received quickfix.fix50.MassQuoteAcknowledgement message: " + massQuoteAck.getQuoteID().getValue());
        
        if (this.twTrade.getMassQuoteAckLatch() != null)
            this.twTrade.getMassQuoteAckLatch().countDown();
    }

}
