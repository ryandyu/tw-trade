package com.sumridge.tw.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.BenchmarkSecurityID;
import quickfix.field.BenchmarkSecurityIDSource;
import quickfix.field.BidPx;
import quickfix.field.BidSize;
import quickfix.field.NoQuoteSets;
import quickfix.field.OfferPx;
import quickfix.field.OfferSize;
import quickfix.field.PriceType;
import quickfix.field.QuoteEntryID;
import quickfix.field.QuoteID;
import quickfix.field.QuoteSetID;
import quickfix.field.QuoteType;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.TotNoQuoteEntries;

import com.sumridge.tw.bean.QuoteEntry;
import com.sumridge.tw.bean.QuoteSet;

public class MassQuoteBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(MassQuoteBuilder.class);

    private String quoteId;
    private int quoteType;
    private List<QuoteSet> quoteSets;

    public MassQuoteBuilder quoteId(String quoteId)
    {
        this.quoteId = quoteId;
        return this;
    }

    public MassQuoteBuilder quoteType(int quoteType)
    {
        this.quoteType = quoteType;
        return this;
    }

    public MassQuoteBuilder quoteSets(List<QuoteSet> quoteSets)
    {
        this.quoteSets = quoteSets;
        return this;
    }

    public quickfix.fix50.MassQuote build()
    {
        LOG.debug("Creating quickfix.fix50.MassQuote object.");
        
        quickfix.fix50.MassQuote massQuote = new quickfix.fix50.MassQuote(new QuoteID(this.quoteId));
        if (this.quoteType == 12)
        {
            massQuote.set(new QuoteType(this.quoteType));
            return massQuote;
        }
        
        massQuote.set(new NoQuoteSets(this.quoteSets.size()));
        for (QuoteSet quoteSet : this.quoteSets)
        {
            quickfix.fix50.MassQuote.NoQuoteSets noQuoteSets = new quickfix.fix50.MassQuote.NoQuoteSets();
            noQuoteSets.set(new QuoteSetID(quoteSet.getQuoteSetId()));
            
            if (quoteSet.getQuoteSetAction() != null)
                noQuoteSets.setString(20182, quoteSet.getQuoteSetAction());
            
            noQuoteSets.setString(SecurityID.FIELD, quoteSet.getSecurityId());
            noQuoteSets.setString(SecurityIDSource.FIELD, quoteSet.getSecurityIdSource());
            noQuoteSets.setInt(20199, quoteSet.getOfferingType());
            noQuoteSets.setInt(6885, quoteSet.getOfferType());
            noQuoteSets.setString(6153, quoteSet.getOwnerTraderId());
            
            if (quoteSet.getBenchmarkSecurityId() != null)
                noQuoteSets.setString(BenchmarkSecurityID.FIELD, quoteSet.getBenchmarkSecurityId());
            
            if (quoteSet.getBenchmarkSecurityIdSource() != null)
                noQuoteSets.setString(BenchmarkSecurityIDSource.FIELD, quoteSet.getBenchmarkSecurityIdSource());
            
            if (quoteSet.getBenchmarkSymbolSfx() != null)
                noQuoteSets.setString(6633, quoteSet.getBenchmarkSymbolSfx());
            
            noQuoteSets.set(new TotNoQuoteEntries(quoteSet.getQuoteEntries().size()));
            
            for (QuoteEntry quoteEntry : quoteSet.getQuoteEntries())
            {
                quickfix.fix50.MassQuote.NoQuoteSets.NoQuoteEntries noQuoteEntries = new quickfix.fix50.MassQuote.NoQuoteSets.NoQuoteEntries();
                noQuoteEntries.set(new QuoteEntryID(quoteEntry.getQuoteEntryId()));
                noQuoteEntries.setBoolean(21027, quoteEntry.isBidEngaged());
                noQuoteEntries.setBoolean(21028, quoteEntry.isOfferEngaged());
                noQuoteEntries.setInt(20189, quoteEntry.getAxeIndicator());
                
                if (quoteEntry.getBidSize() > 0)
                {
                    switch (quoteSet.getOfferType())
                    {
                        case PriceType.PERCENTAGE:
                            noQuoteEntries.setDouble(20192, quoteEntry.getBidPx());
                            break;
                        default:
                            noQuoteEntries.set(new BidPx(quoteEntry.getBidPx()));
                            break;
                    }
                    noQuoteEntries.set(new BidSize(quoteEntry.getBidSize()));
                }
                
                if (quoteEntry.getOfferSize() > 0)
                {
                    switch (quoteSet.getOfferType())
                    {
                        case PriceType.PERCENTAGE:
                            noQuoteEntries.setDouble(20193, quoteEntry.getOfferPx());
                            break;
                        default:
                            noQuoteEntries.set(new OfferPx(quoteEntry.getOfferPx()));
                            break;
                    }
                    noQuoteEntries.set(new OfferSize(quoteEntry.getOfferSize()));
                }
                
                noQuoteSets.addGroup(noQuoteEntries);
            }
            
            massQuote.addGroup(noQuoteSets);
        }
        
        return massQuote;
    }
}
