package com.sumridge.tw.builder;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.QuoteID;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteRespID;
import quickfix.field.QuoteStatus;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class QuoteStatusReportBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteStatusReportBuilder.class);

    private String quoteReqId, quoteRespId;
    private String quoteId;
    private java.util.Date transactTime;
    private int quoteStatus;
    private String ownerTraderId;
    private String text;
    private String symbol;

    public QuoteStatusReportBuilder quoteReqId(String quoteReqId)
    {
        this.quoteReqId = quoteReqId;
        return this;
    }

    public QuoteStatusReportBuilder quoteRespId(String quoteRespId)
    {
        this.quoteRespId = quoteRespId;
        return this;
    }

    public QuoteStatusReportBuilder quoteId(String quoteId)
    {
        this.quoteId = quoteId;
        return this;
    }

    public QuoteStatusReportBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public QuoteStatusReportBuilder quoteStatus(int quoteStatus)
    {
        this.quoteStatus = quoteStatus;
        return this;
    }

    public QuoteStatusReportBuilder ownerTraderId(String ownerTraderId)
    {
        this.ownerTraderId = ownerTraderId;
        return this;
    }

    public QuoteStatusReportBuilder text(String text)
    {
        this.text = text;
        return this;
    }

    public QuoteStatusReportBuilder symbol(String symbol)
    {
        this.symbol = symbol;
        return this;
    }

    public quickfix.fix50.QuoteStatusReport build()
    {
        LOG.debug("Creating quickfix.fix50.QuoteStatusReport object.");
        
        quickfix.fix50.QuoteStatusReport quoteStatusReport = new quickfix.fix50.QuoteStatusReport();
        
        if (this.quoteReqId != null)
            quoteStatusReport.set(new QuoteReqID(this.quoteReqId));
        else
            quoteStatusReport.set(new QuoteRespID(this.quoteRespId));
        
        if (this.quoteId != null)
            quoteStatusReport.set(new QuoteID(this.quoteId));
        
        quoteStatusReport.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        quoteStatusReport.set(new QuoteStatus(this.quoteStatus));
        
        if (this.ownerTraderId != null)
            quoteStatusReport.setString(6153, this.ownerTraderId);
        
        if (this.text != null)
            quoteStatusReport.set(new Text(this.text));
        
        if (this.symbol != null)
            quoteStatusReport.set(new Symbol(this.symbol));
        
        return quoteStatusReport;
    }

}
