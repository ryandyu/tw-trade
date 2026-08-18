package com.sumridge.tw.builder;

import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.PriceType;
import quickfix.field.QuoteID;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteRespID;
import quickfix.field.QuoteType;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class QuoteBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteBuilder.class);

    private String quoteReqId, quoteRespId;
    private String quoteId;
    private int quoteType;
    private String symbol;
    private java.util.Date transactTime;
    private String ownerTraderId;
    private String clientFreeText1, clientFreeText2;
    private char side;
    private int priceType;
    private double price;
    private double orderQty;
    private String revealOption;
    private String confCoriBook, confTrsyBook;
    private List<quickfix.fix50.Quote.NoLegs> noLegs;
    private String text;

    public QuoteBuilder quoteReqId(String quoteReqId)
    {
        this.quoteReqId = quoteReqId;
        return this;
    }

    public QuoteBuilder quoteRespId(String quoteRespId)
    {
        this.quoteRespId = quoteRespId;
        return this;
    }

    public QuoteBuilder quoteId(String quoteId)
    {
        this.quoteId = quoteId;
        return this;
    }

    public QuoteBuilder quoteType(int quoteType)
    {
        this.quoteType = quoteType;
        return this;
    }

    public QuoteBuilder symbol(String symbol)
    {
        this.symbol = symbol;
        return this;
    }

    public QuoteBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public QuoteBuilder ownerTraderId(String ownerTraderId)
    {
        this.ownerTraderId = ownerTraderId;
        return this;
    }

    public QuoteBuilder clientFreeText1(String clientFreeText1)
    {
        this.clientFreeText1 = clientFreeText1;
        return this;
    }

    public QuoteBuilder clientFreeText2(String clientFreeText2)
    {
        this.clientFreeText2 = clientFreeText2;
        return this;
    }

    public QuoteBuilder side(char side)
    {
        this.side = side;
        return this;
    }

    public QuoteBuilder priceType(int priceType)
    {
        this.priceType = priceType;
        return this;
    }

    public QuoteBuilder price(double price)
    {
        this.price = price;
        return this;
    }

    public QuoteBuilder orderQty(double orderQty)
    {
        this.orderQty = orderQty;
        return this;
    }

    public QuoteBuilder revealOption(String revealOption)
    {
        this.revealOption = revealOption;
        return this;
    }

    public QuoteBuilder confCoriBook(String confCoriBook)
    {
        this.confCoriBook = confCoriBook;
        return this;
    }

    public QuoteBuilder confTrsyBook(String confTrsyBook)
    {
        this.confTrsyBook = confTrsyBook;
        return this;
    }

    public QuoteBuilder noLegs(List<quickfix.fix50.Quote.NoLegs> noLegs)
    {
        this.noLegs = noLegs;
        return this;
    }

    public QuoteBuilder text(String text)
    {
        this.text = text;
        return this;
    }

    public quickfix.fix50.Quote build()
    {
        LOG.debug("Creating quickfix.fix50.Quote object.");
        
        quickfix.fix50.Quote quote = new quickfix.fix50.Quote();
        
        if (this.quoteReqId != null)
            quote.set(new QuoteReqID(this.quoteReqId));
        else
            quote.set(new QuoteRespID(this.quoteRespId));
        
        quote.set(new QuoteID(this.quoteId));
        quote.set(new QuoteType(this.quoteType));
        quote.set(new Symbol(this.symbol));
        quote.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        quote.setString(6153, this.ownerTraderId);
        
        if (this.clientFreeText1 != null)
            quote.setString(9596, this.clientFreeText1);
        
        if (this.clientFreeText2 != null)
            quote.setString(9597, this.clientFreeText2);
        
        if (this.side != 0)
            quote.set(new Side(this.side));
        
        if (this.priceType != 0)
            quote.set(new PriceType(this.priceType));
        
        if (this.price != 0)
            quote.setDouble(Price.FIELD, this.price);
        
        if (this.orderQty != 0)
            quote.set(new OrderQty(this.orderQty));
        
        if (this.revealOption != null)
            quote.setString(20051, this.revealOption);
        
        if (this.confCoriBook != null)
            quote.setString(22631, this.confCoriBook);
        
        if (this.confTrsyBook != null)
            quote.setString(22632, this.confTrsyBook);
        
        if (this.noLegs != null)
            for (quickfix.fix50.Quote.NoLegs noLeg : this.noLegs)
                quote.addGroup(noLeg);
        
        if (this.text != null)
            quote.set(new Text(this.text));
        
        return quote;
    }

}
