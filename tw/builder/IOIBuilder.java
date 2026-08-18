package com.sumridge.tw.builder;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import quickfix.field.BenchmarkSecurityID;
import quickfix.field.BenchmarkSecurityIDSource;
import quickfix.field.IOIQty;
import quickfix.field.IOIRefID;
import quickfix.field.IOITransType;
import quickfix.field.IOIID;
import quickfix.field.NoRoutingIDs;
import quickfix.field.Price;
import quickfix.field.PriceType;

import quickfix.field.RoutingID;
import quickfix.field.RoutingType;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.Side;
import quickfix.field.Spread;
import quickfix.field.Symbol;
import quickfix.field.Yield;
import quickfix.fix50.IOI;

//import com.sumridge.tw.bean.QuoteEntry;
//import com.sumridge.tw.bean.QuoteSet;
//import com.sumridge.tw.bean.SecurityStatus;
import com.sumridge.tw.bean.TWTrade;
import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.offer.Offer;
import com.sumridge.xml.jaxb.offer.QuoteType;
//import com.sumridge.tw.bean.QuoteEntry.QuoteEntryBuilder;
//import com.sumridge.tw.bean.QuoteSet.QuoteSetBuilder;



public class IOIBuilder
{

	    private static final Logger LOG = LoggerFactory.getLogger(IOIBuilder.class);

	    private String quoteId;
	    private int quoteType;
//	    private List<QuoteSet> quoteSets = new LinkedList<QuoteSet>();

	    
	    public IOIBuilder quoteId(String quoteId)
	    {
	        this.quoteId = quoteId;
	        return this;
	    }

	    public IOIBuilder quoteType(int quoteType)
	    {
	        this.quoteType = quoteType;
	        return this;
	    }

//	    public IOIBuilder quoteSets(List<QuoteSet> quoteSets)
//	    {
//	        this.quoteSets = quoteSets;
//	        return this;
//	    }

	    @SuppressWarnings({ "finally"})
		public IOI build(Offer mktOffer, TWTrade twTrade, String blockFirm)  {
	        
		    IOI ioi = null;
		    
		    try {
		    	
		    		String cusip = XmlJaxbUtils.getCusip(mktOffer.getSecurityIds());
		            String accountId = StringUtils.stripToNull(mktOffer.getAccountId());
//		            SecurityStatus securityStatus = twTrade.getSecurities().get(cusip);	    	    
//		            if (securityStatus == null)
//		            {
//		                LOG.warn("Unsupported CUSIP: " + cusip);
//		    			return ioi;	                
//		            }	            
		            String ownerTraderId = twTrade.getTraderAccounts().get(accountId);	            
		            if (ownerTraderId == null)
		            {
		                LOG.warn("Trader not setup for account: " + accountId);
		    			return ioi;
		            }
		/*    		if (securityStatus.isOmsFlagOn() == false && (mktOffer.getRemarks() == null || mktOffer.getRemarks().indexOf('W') < 0) ) {
		                LOG.warn("OMS flag off: " + cusip);
		    			return ioi;	                	    			
		    		}	            
		    		else
		    			securityStatus.setOmsFlag(true);*/
		    		
		    		
		    	    Offer offer = twTrade.getOffers().get(cusip);    
		    	    if (offer ==  null)  {
		    	    	LOG.warn("No offer in cache for  " + cusip);
		    			return ioi;
		    	    }
		    	    
		    	    //blockFirm = query.findBlockFirm(accountId);	            
		            boolean isSpreadBased;
		            String benchmarkSecurityId = "";
		            
		            int offerType =  offer.getSpreadIssue() != null ?  PriceType.SPREAD : PriceType.PERCENTAGE ; /*securityStatus.getOfferType()*/;
		            switch (offerType)
		            {
		                case PriceType.PERCENTAGE:
		                    isSpreadBased = false;
		                    break;
		                
		                case PriceType.SPREAD:
		                    isSpreadBased = true;
		                
		                    if (offer.getSpreadIssue() != null)
		                        benchmarkSecurityId = StringUtils.stripToEmpty(XmlJaxbUtils.getCusip(offer.getSpreadIssue().getSecurityIds()));
		                    
//		                    if (!benchmarkSecurityId.equals(securityStatus.getBenchmarkSecurityId()))
//		                    {
//		                        LOG.warn(String.format("Benchmark id mismatch: %s != %s", benchmarkSecurityId, securityStatus.getBenchmarkSecurityId()));
//		            			return ioi;
//		                    }
		                    break;
		            
		                 default:
		        			return ioi;
		            }
		            		           
		            List<QuoteType> bids = mktOffer.getQuotes().getBids();
		            List<QuoteType> asks = mktOffer.getQuotes().getAsks();

		            Side s ;
		            QuoteType quoteType;
		            if (bids != null && bids.size() > 0 && bids.get(0).getQty() != null && bids.get(0).getQty()  > 0)  {
		            	s = new Side(Side.BUY);
		            	quoteType = bids.get(0);
		            }
		            else  if (asks != null && asks.size() > 0 && asks.get(0).getQty() != null && asks.get(0).getQty() > 0) {
		            	s = new Side(Side.SELL);
		            	quoteType = asks.get(0);
		            }
		            else
		            	return ioi;
		            		                
		            double price = 0;
		            int size = 0;
		                
		            if (isSpreadBased && quoteType.getSpread() != null)
		                  price = BigDecimal.valueOf(quoteType.getSpread().doubleValue() / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
		            else if (!isSpreadBased && quoteType.getPrice() != null && quoteType.getPrice() > 0)
		                  price = BigDecimal.valueOf(quoteType.getPrice().doubleValue()).setScale(4, RoundingMode.HALF_UP).doubleValue();

		            size = quoteType.getQty().intValue();
		               		            
		            String quoteSetId = String.format("%s-%s-%d-%s", ownerTraderId, cusip, offerType, s.getValue());
//		            QuoteSet quoteSet = twTrade.getQuoteSets().get(quoteSetId);
//		            List<QuoteEntry> quoteEntries;
//		            
//		            if (quoteSet != null)   {
//		            	 quoteSet.setQuoteSetAction("UPD");
//		            	 quoteEntries = quoteSet.getQuoteEntries();
//		            }
//		            else  {
//		            	quoteEntries = new LinkedList<QuoteEntry>();
//		            	quoteSet = new QuoteSetBuilder(UUID.randomUUID().toString())
//                        .quoteSetAction("NEW").securityId(cusip)
//                        .securityIdSource(SecurityIDSource.CUSIP)
//                        .offeringType(102).offerType(offerType)
//                        .ownerTraderId(ownerTraderId)
//                        .benchmarkSecurityId(StringUtils.stripToNull(benchmarkSecurityId))
//                        .benchmarkSecurityIdSource(SecurityIDSource.CUSIP)
//                        .quoteEntries(quoteEntries)
//                        .build();
//		            }
		            
	            	
//		            quoteEntries.clear();
//	            	if (s.getValue() == Side.BUY)    {
//	                    quoteEntries.add(
//	                            new QuoteEntryBuilder(new Integer(1).toString())
//	                            .bidPx(price)
//	                            .offerPx(0.0)
//	                            .bidSize(size)
//	                            .offerSize(0)
//	                            .build()
//	                            );	            	
//	                 }
//	                 else  {
//	                     quoteEntries.add(
//	                             new QuoteEntryBuilder(new Integer(2).toString())
//	                             .bidPx(0.0)
//	                             .offerPx(price)
//	                             .bidSize(0)
//	                             .offerSize(size)
//	                             .build()
//	                             );	                 
//	                 }

                    
//			        quoteSets.add(quoteSet);    
//		            twTrade.getQuoteSets().put(quoteSetId, quoteSet);
		            
//		            ioi = new IOIBuilder().quoteId(UUID.randomUUID().toString()).quoteSets(quoteSets).build(s, blockFirm);
		           		         
		        
		    }catch (Exception e) {
		    	e.printStackTrace();
		    }
		    finally {		        
		        return ioi;
		    }		        
	    }
	    
	  
	    public IOI build(Side s, String blockFirms)  {
	    	
	    	LOG.debug("Creating quickfix.fix50.IOI object.");
	        IOI ioi = new IOI();

//	        QuoteSet quoteSet = quoteSets.get(0);

	        ioi.setString(Symbol.FIELD, "[N/A]");
            ioi.setString(IOIID.FIELD, quoteId/*quoteSet.getQuoteSetId()*/);
            
//            String action = quoteSet.getQuoteSetAction();
//            
//            if (action != null)  {
//            	if ("UPD".equals(action))   {
//            		ioi.setChar(IOITransType.FIELD, IOITransType.REPLACE);
//            		ioi.setString(IOIRefID.FIELD, quoteSet.getQuoteSetId());
//            	}
//            	else if ("DEL".equals(action))   {
//            		ioi.setChar(IOITransType.FIELD, IOITransType.CANCEL);
//            		ioi.setString(IOIRefID.FIELD, quoteSet.getQuoteSetId());
//            	}
//            	else
//            		ioi.setChar(IOITransType.FIELD, IOITransType.NEW);
//            }	        

//            ioi.setChar(Side.FIELD, s.getValue());
//            ioi.setString(SecurityID.FIELD, quoteSet.getSecurityId());
//            ioi.setString(SecurityIDSource.FIELD, quoteSet.getSecurityIdSource());
//            ioi.setString(6153, quoteSet.getOwnerTraderId());
//            
//            if (blockFirms != null &&  blockFirms.length() > 0 ) {
//            	ioi.setInt(NoRoutingIDs.FIELD, 1);
//            	ioi.setInt(RoutingType.FIELD, RoutingType.TARGET_FIRM);
//            	ioi.setString(RoutingID.FIELD, blockFirms);
//            }
//            
//            if (quoteSet.getBenchmarkSecurityId() != null)
//                ioi.setString(BenchmarkSecurityID.FIELD, quoteSet.getBenchmarkSecurityId());
//            
//            if (quoteSet.getBenchmarkSecurityIdSource() != null)
//                ioi.setString(BenchmarkSecurityIDSource.FIELD, quoteSet.getBenchmarkSecurityIdSource());
//            
//           
//            	QuoteEntry quoteEntry = quoteSet.getQuoteEntries().get(0);
//               
//                double px =  s.equals(new Side(Side.BUY)) ? quoteEntry.getBidPx() : quoteEntry.getOfferPx() ;
//                int qty = s.equals(new Side(Side.BUY)) ? quoteEntry.getBidSize() :  quoteEntry.getOfferSize();
//	                
//                              
//                if (qty > 0)
//                {
//                    switch (quoteSet.getOfferType())
//                    {
//                        case PriceType.PERCENTAGE:
//                            ioi.setDouble(Price.FIELD, px);
//                            ioi.setInt(PriceType.FIELD, 1);
//                            break;
//                        case PriceType.SPREAD:
//                            ioi.setDouble(Spread.FIELD, px);
//                            ioi.setInt(PriceType.FIELD, 6);
//                            break;  
//                        case PriceType.YIELD:
//                            ioi.setDouble(Yield.FIELD, px);
//                            ioi.setInt(PriceType.FIELD, 9);
//                            break;                                 
//                        default:
//                        	break;	
//                    }
//                    
//                    ioi.setInt(IOIQty.FIELD, qty);	
//                }
           
            
            return ioi;
		}
	
}


