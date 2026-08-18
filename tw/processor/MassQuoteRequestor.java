package com.sumridge.tw.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.PriceType;
import quickfix.field.SecurityIDSource;
import quickfix.fix50.MassQuote;

import com.sumridge.tw.bean.QuoteEntry;
import com.sumridge.tw.bean.QuoteEntry.QuoteEntryBuilder;
import com.sumridge.tw.bean.QuoteSet;
import com.sumridge.tw.bean.QuoteSet.QuoteSetBuilder;
import com.sumridge.tw.bean.SecurityStatus;
import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.MassQuoteBuilder;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.offer.Offer;
import com.sumridge.xml.jaxb.offer.QuoteType;

@Component
public class MassQuoteRequestor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(MassQuoteRequestor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing com.sumridge.xml.jaxb.offer.Offer[] message.");
        
        Offer[] offers = exchange.getIn().getBody(Offer[].class);
        
        List<QuoteSet> quoteSets = new LinkedList<QuoteSet>(), updateQuoteSets = new LinkedList<QuoteSet>();
        
        for (Offer offer : offers)
        {
            String cusip = XmlJaxbUtils.getCusip(offer.getSecurityIds());
            String accountId = StringUtils.stripToNull(offer.getAccountId());
            SecurityStatus securityStatus = this.twTrade.getSecurities().get(cusip);
        
            if (securityStatus == null)
            {
                LOG.warn("Unsupported CUSIP: " + cusip);
                continue;
            }
            
            String ownerTraderId = this.twTrade.getTraderAccounts().get(accountId);
            
            if (ownerTraderId == null)
            {
                LOG.warn("Trader not setup for account: " + accountId);
                continue;
            }
            
            boolean isSpreadBased;
            String benchmarkSecurityId = "";
            
            int offerType = securityStatus.getOfferType();
            switch (offerType)
            {
                case PriceType.PERCENTAGE:
                    isSpreadBased = false;
                    break;
                
                case PriceType.SPREAD:
                    isSpreadBased = true;
                
                    if (offer.getSpreadIssue() != null)
                        benchmarkSecurityId = StringUtils.stripToEmpty(XmlJaxbUtils.getCusip(offer.getSpreadIssue().getSecurityIds()));
                    
                    if (!benchmarkSecurityId.equals(securityStatus.getBenchmarkSecurityId()))
                    {
                        LOG.warn(String.format("Benchmark id mismatch: %s != %s", benchmarkSecurityId, securityStatus.getBenchmarkSecurityId()));
                        continue;
                    }
                    break;
            
                default:
                    continue;
            }
            
            List<QuoteEntry> quoteEntries = new LinkedList<QuoteEntry>();
            for (int i = 1; i <= 3; i++)
            {
                if (i != this.twTrade.getPriceLevel())
                    continue;
                
                double bidPx = 0, offerPx = 0;
                int bidSize = 0, offerSize = 0;
                
                for (QuoteType quoteType : offer.getQuotes().getAsks())
                {
                    if (i != quoteType.getLevel())
                        continue;
                
                    if (isSpreadBased && quoteType.getSpread() != null)
                        offerPx = BigDecimal.valueOf(quoteType.getSpread().doubleValue() / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    else if (!isSpreadBased && quoteType.getPrice() != null && quoteType.getPrice() > 0)
                        offerPx = BigDecimal.valueOf(quoteType.getPrice().doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue();
                    else
                        continue;
                    
                    if (quoteType.getQty() != null)
                        offerSize = quoteType.getQty().intValue();
                }
                
                for (QuoteType quoteType : offer.getQuotes().getBids())
                {
                    if (i != quoteType.getLevel())
                        continue;
                
                    if (isSpreadBased && quoteType.getSpread() != null)
                        bidPx = BigDecimal.valueOf(quoteType.getSpread().doubleValue() / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    else if (!isSpreadBased && quoteType.getPrice() != null && quoteType.getPrice() > 0)
                        bidPx = BigDecimal.valueOf(quoteType.getPrice().doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue();
                    else
                        continue;
                    
                    if (quoteType.getQty() != null)
                        bidSize = quoteType.getQty().intValue();
                }
                
                if (bidSize > 0 || offerSize > 0)
                    quoteEntries.add(
                            new QuoteEntryBuilder(new Integer(i).toString())
                            .bidPx(bidPx)
                            .offerPx(offerPx)
                            .bidSize(bidSize)
                            .offerSize(offerSize)
                            .build()
                            );
            }
            
            String quoteSetId = String.format("%s-%s-%d", ownerTraderId, cusip, offerType);
            QuoteSet quoteSet = this.twTrade.getQuoteSets().get(quoteSetId);
            
            if ("1".equals(offer.getType()) && !quoteEntries.isEmpty())
            {
                QuoteSet updateQuoteSet = new QuoteSetBuilder(UUID.randomUUID().toString())
                        .quoteSetAction("UPD").securityId(cusip)
                        .securityIdSource(SecurityIDSource.CUSIP)
                        .offeringType(102).offerType(offerType)
                        .ownerTraderId(ownerTraderId)
                        .benchmarkSecurityId(StringUtils.stripToNull(benchmarkSecurityId))
                        .benchmarkSecurityIdSource(SecurityIDSource.CUSIP)
                        .quoteEntries(quoteEntries)
                        .build();
                
                if (quoteSet != null)
                {
                    quoteSet.setQuoteSetAction("DEL");
                    quoteSets.add(quoteSet);
                    updateQuoteSets.add(updateQuoteSet);
                }
                else
                    quoteSets.add(updateQuoteSet);
                
                this.twTrade.getQuoteSets().put(quoteSetId, updateQuoteSet);
            }
            else if (quoteSet != null)
            {
                this.twTrade.getQuoteSets().remove(quoteSetId);
                quoteSet.setQuoteSetAction("DEL");
                quoteSets.add(quoteSet);
            }
        }
        
        List<MassQuote> massQuotes = new LinkedList<MassQuote>();
        
        if (!quoteSets.isEmpty())
        {
            massQuotes.add(new MassQuoteBuilder().quoteId(UUID.randomUUID().toString()).quoteSets(quoteSets).build());

            if (!updateQuoteSets.isEmpty())
                massQuotes.add(new MassQuoteBuilder().quoteId(UUID.randomUUID().toString()).quoteSets(updateQuoteSets).build());
        }
        
        exchange.getIn().setBody(massQuotes);
    }
}
