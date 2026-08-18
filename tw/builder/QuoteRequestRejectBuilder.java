package com.sumridge.tw.builder;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.NoRelatedSym;
import quickfix.field.QuoteID;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteRequestRejectReason;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class QuoteRequestRejectBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRequestRejectBuilder.class);

    private String quoteReqId;
    private int quoteRequestRejectReason;
    private String quoteId;
    private int noRelatedSym;
    private java.util.Date transactTime;
    private String ownerTraderId;
    private String text;

    public QuoteRequestRejectBuilder quoteReqId(String quoteReqId)
    {
        this.quoteReqId = quoteReqId;
        return this;
    }

    public QuoteRequestRejectBuilder quoteRequestRejectReason(int quoteRequestRejectReason)
    {
        this.quoteRequestRejectReason = quoteRequestRejectReason;
        return this;
    }

    public QuoteRequestRejectBuilder quoteId(String quoteId)
    {
        this.quoteId = quoteId;
        return this;
    }

    public QuoteRequestRejectBuilder noRelatedSym(int noRelatedSym)
    {
        this.noRelatedSym = noRelatedSym;
        return this;
    }

    public QuoteRequestRejectBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public QuoteRequestRejectBuilder ownerTraderId(String ownerTraderId)
    {
        this.ownerTraderId = ownerTraderId;
        return this;
    }

    public QuoteRequestRejectBuilder text(String text)
    {
        this.text = text;
        return this;
    }

    public quickfix.fix50.QuoteRequestReject build()
    {
        LOG.debug("Creating quickfix.fix50.QuoteRequestReject message.");
        
        quickfix.fix50.QuoteRequestReject quoteReqRej = new quickfix.fix50.QuoteRequestReject();
        quoteReqRej.set(new QuoteReqID(this.quoteReqId));
        quoteReqRej.set(new QuoteRequestRejectReason(this.quoteRequestRejectReason));
        
        if (this.quoteId != null)
            quoteReqRej.setString(QuoteID.FIELD, this.quoteId);
        
        quoteReqRej.set(new NoRelatedSym(this.noRelatedSym));
        quoteReqRej.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        
        if (this.ownerTraderId != null)
            quoteReqRej.setString(6153, this.ownerTraderId);
        
        if (this.text != null)
            quoteReqRej.set(new Text(this.text));
        
        return quoteReqRej;
    }

}
