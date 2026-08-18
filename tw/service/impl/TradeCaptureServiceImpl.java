package com.sumridge.tw.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.etrade.ws.StsOrderWebServiceInterface;
import com.sumridge.tw.service.TradeCaptureService;
import com.sumridge.xml.jaxb.trade.TradeCapture;

@Service
public class TradeCaptureServiceImpl implements TradeCaptureService
{

    private static final Logger logger = LoggerFactory.getLogger(TradeCaptureServiceImpl.class);

    @Autowired
    private StsOrderWebServiceInterface stsOrderWebServiceInterface;

    @Override
    public void createTradeCapture(TradeCapture tradeCapture)
    {
        try
        {
            this.stsOrderWebServiceInterface.executeDealerTrade(tradeCapture);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
