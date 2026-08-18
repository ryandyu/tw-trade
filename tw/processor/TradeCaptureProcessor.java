package com.sumridge.tw.processor;

import org.apache.camel.Handler;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.service.TradeCaptureService;
import com.sumridge.xml.jaxb.trade.TradeCapture;

@Component
public class TradeCaptureProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeCaptureProcessor.class);

    @Autowired
    private TradeCaptureService tradeCaptureService;

    @Handler
    public void process(TradeCapture tradeCapture) throws Exception
    {
        LOG.debug("Executing dealer trade.");
        
        try
        {
            this.tradeCaptureService.createTradeCapture(tradeCapture);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

}
