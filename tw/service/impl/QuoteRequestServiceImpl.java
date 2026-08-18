package com.sumridge.tw.service.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.etrade.ws.StsOrderWebServiceInterface;
import com.sumridge.tw.service.QuoteRequestService;
import com.sumridge.xml.jaxb.order.QuoteRequest;

@Service
public class QuoteRequestServiceImpl implements QuoteRequestService
{

    private static final Logger logger = LoggerFactory.getLogger(QuoteRequestServiceImpl.class);

    @Autowired
    private StsOrderWebServiceInterface stsOrderWebServiceInterface;

    @Override
    public void createQuoteRequest(QuoteRequest quoteRequest)
    {
        logger.debug("Adding quote request.");
        try
        {
            this.stsOrderWebServiceInterface.addQuoteRequest(quoteRequest);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateQuoteRequestStatus(QuoteRequest quoteRequest)
    {
        logger.debug("Updating quote request status.");
        try
        {
            this.stsOrderWebServiceInterface.updateQuoteRequestStatus2(quoteRequest);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateQuoteRequest(QuoteRequest quoteRequest)
    {
        logger.debug("Updating quote request.");
        try
        {
            this.stsOrderWebServiceInterface.updateQuoteRequestByPlatformId(quoteRequest);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuoteRequest getQuoteRequest(String quoteReqId)
    {
        logger.debug("Retrieving quote request.");
        try
        {
            return this.stsOrderWebServiceInterface.getQuoteRequest(NumberUtils.toInt(quoteReqId));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
