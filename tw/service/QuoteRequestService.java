package com.sumridge.tw.service;

import com.sumridge.xml.jaxb.order.QuoteRequest;

public interface QuoteRequestService
{

    public void createQuoteRequest(QuoteRequest quoteRequest);

    public void updateQuoteRequestStatus(QuoteRequest quoteRequest);

    public void updateQuoteRequest(QuoteRequest quoteRequest);

    public QuoteRequest getQuoteRequest(String quoteReqId);

}
