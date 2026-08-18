package com.sumridge.tw.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.etrade.ws.StsOrderWebServiceInterface;
import com.sumridge.tw.service.TradeRequestService;
import com.sumridge.xml.jaxb.order.TradeRequest;

@Service
public class TradeRequestServiceImpl implements TradeRequestService
{

    private static final Logger logger = LoggerFactory.getLogger(TradeRequestServiceImpl.class);

    @Autowired
    private StsOrderWebServiceInterface stsOrderWebServiceInterface;

    @Override
    public void createTradeRequest(TradeRequest tradeRequest)
    {
        logger.debug("Updating trade request.");
        try
        {
            this.stsOrderWebServiceInterface.updateTradeRequestByPlatformId(tradeRequest);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
