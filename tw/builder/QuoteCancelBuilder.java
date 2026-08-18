package com.sumridge.tw.builder;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.QuoteCancelType;
import quickfix.field.QuoteID;
import quickfix.field.QuoteReqID;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class QuoteCancelBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteCancelBuilder.class);

    private String quoteReqId;
    private String quoteId;
    private int quoteCancelType;
    private java.util.Date transactTime;
    private String ownerTraderId;
    private String text;

    public QuoteCancelBuilder quoteReqId(String quoteReqId)
    {
        this.quoteReqId = quoteReqId;
        return this;
    }

    public QuoteCancelBuilder quoteId(String quoteId)
    {
        this.quoteId = quoteId;
        return this;
    }

    public QuoteCancelBuilder quoteCancelType(int quoteCancelType)
    {
        this.quoteCancelType = quoteCancelType;
        return this;
    }

    public QuoteCancelBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public QuoteCancelBuilder ownerTraderId(String ownerTraderId)
    {
        this.ownerTraderId = ownerTraderId;
        return this;
    }

    public QuoteCancelBuilder text(String text)
    {
        this.text = text;
        return this;
    }

    public quickfix.fix50.QuoteCancel build()
    {
        LOG.debug("Creating quickfix.fix50.QuoteCancel object.");
     
        quickfix.fix50.QuoteCancel quoteCancel = new quickfix.fix50.QuoteCancel();
        quoteCancel.set(new QuoteReqID(this.quoteReqId));
        
        if (this.quoteId != null)
            quoteCancel.set(new QuoteID(this.quoteId));
        
        quoteCancel.set(new QuoteCancelType(this.quoteCancelType));
        quoteCancel.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        
        if (this.ownerTraderId != null)
            quoteCancel.setString(6153, this.ownerTraderId);
        
        if (this.text != null)
            quoteCancel.setString(Text.FIELD, this.text);
        
        return quoteCancel;
    }
}
