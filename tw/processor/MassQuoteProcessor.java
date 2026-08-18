package com.sumridge.tw.processor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.bean.TWTrade;

@Component
public class MassQuoteProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(MassQuoteProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.MassQuote message.");

        if (this.twTrade.getMassQuoteAckLatch() != null)
        {
            try
            {
                if (!this.twTrade.getMassQuoteAckLatch().await(15L, TimeUnit.SECONDS))
                    throw new IllegalStateException("Message did not reach market!");
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage(), e);
                throw ObjectHelper.wrapRuntimeCamelException(e);
            }
        }

        this.twTrade.setMassQuoteAckLatch(new CountDownLatch(1));
    }

}
