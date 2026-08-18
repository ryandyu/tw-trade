package com.sumridge.tw.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sumridge.tw.service.TradePlatformService;
import com.sumridge.xml.jaxb.common.PriceType;
import com.sumridge.xml.jaxb.offer.Offer;

@Component
public class TWTrade
{

    @Value("${platform_id}")
    private Integer platformId;
   
    @Resource(name = "traderIds")
    private Properties traderIds;
    @Resource(name = "accountIds")
    private Properties accountIds;
    @Resource(name = "partyIds")
    private Properties partyIds;
    
    private Map<String, PriceType> counteredQuotes = new ConcurrentHashMap<String, PriceType>();
    private Map<String, SecurityStatus> securities = new ConcurrentHashMap<String, SecurityStatus>();
    private Map<String, Offer> offers = new ConcurrentHashMap<String, Offer>();
    
    private Map<String, Boolean> deskFilter = new ConcurrentHashMap<String, Boolean>()
                               , accountFilter = new ConcurrentHashMap<String, Boolean>()
                               , secIdFilter = new ConcurrentHashMap<String, Boolean>();
    
    private Map<String, String> ownerTraderIds = new HashMap<String, String>()
                              , rootPartyIds = new HashMap<String, String>()
                              , traderAccounts = new HashMap<String, String>();
    
    private Map<String, QuoteSet> quoteSets = new ConcurrentHashMap<String, QuoteSet>();
    
    private CountDownLatch massQuoteAckLatch;
    private Integer priceLevel;
    
    @Autowired
    private TradePlatformService tradePlatformService;

    
    @PostConstruct
    public void init()
    {
        //ownerTraderIds : book --> twId from traderIds.properties  
        //rootPartyIds: twId --> book mapping reversed from traderIds.properties
        for (String book : this.traderIds.stringPropertyNames())
        {
            String traderId = this.traderIds.getProperty(book, "");
            
            this.getOwnerTraderIds().put(book, traderId);  //book --> twId
            
            if (!"NA".equals(book))
                this.getRootPartyIds().put(traderId, book); //twId --> book
        }

        //accountId --> book from accountIds.properties
        //book --> twId from ownerTraderIds mapping
        //traderAccounts: accountId --> twId
        for (String accountId : this.accountIds.stringPropertyNames())
        {
            String book = this.accountIds.getProperty(accountId, "");
            
            String twId = this.getOwnerTraderIds().get(book);
            
            this.getTraderAccounts().put(accountId, twId);  //accountId --> twId
        }
        
        //additional twId --> book from rootPartyIds.properties file
        for( String twId : this.partyIds.stringPropertyNames())
        {
            String book = this.partyIds.getProperty(twId, "NA");

            if (!"NA".equals(book))
                this.getRootPartyIds().put(twId, book); //twID --> book override if need
        }
        
        this.setPriceLevel(this.tradePlatformService.findTierPriceLevel(this.platformId));
    }

    public String getBookName(String accountId)
    {
        return this.accountIds.getProperty(accountId, "NA");
    }

    public Integer getPlatformId()
    {
        return platformId;
    }

    public void setPlatformId(Integer platformId)
    {
        this.platformId = platformId;
    }

    public Map<String, PriceType> getCounteredQuotes()
    {
        return counteredQuotes;
    }

    public void setCounteredQuotes(Map<String, PriceType> counteredQuotes)
    {
        this.counteredQuotes = counteredQuotes;
    }

    public Map<String, SecurityStatus> getSecurities()
    {
        return securities;
    }

    public void setSecurities(Map<String, SecurityStatus> securities)
    {
        this.securities = securities;
    }

    public Map<String, Offer> getOffers()
    {
        return offers;
    }

    public void setOffers(Map<String, Offer> offers)
    {
        this.offers = offers;
    }

    public Map<String, Boolean> getDeskFilter()
    {
        return deskFilter;
    }

    public void setDeskFilter(Map<String, Boolean> deskFilter)
    {
        this.deskFilter = deskFilter;
    }

    public Map<String, Boolean> getAccountFilter()
    {
        return accountFilter;
    }

    public void setAccountFilter(Map<String, Boolean> accountFilter)
    {
        this.accountFilter = accountFilter;
    }

    public Map<String, Boolean> getSecIdFilter()
    {
        return secIdFilter;
    }

    public void setSecIdFilter(Map<String, Boolean> secIdFilter)
    {
        this.secIdFilter = secIdFilter;
    }

    public Map<String, String> getOwnerTraderIds()
    {
        return ownerTraderIds;
    }

    public void setOwnerTraderIds(Map<String, String> ownerTraderIds)
    {
        this.ownerTraderIds = ownerTraderIds;
    }

    public Map<String, String> getRootPartyIds()
    {
        return rootPartyIds;
    }

    public void setRootPartyIds(Map<String, String> rootPartyIds)
    {
        this.rootPartyIds = rootPartyIds;
    }

    public Map<String, String> getTraderAccounts()
    {
        return traderAccounts;
    }

    public void setTraderAccounts(Map<String, String> traderAccounts)
    {
        this.traderAccounts = traderAccounts;
    }

    public Map<String, QuoteSet> getQuoteSets()
    {
        return quoteSets;
    }

    public void setQuoteSets(Map<String, QuoteSet> quoteSets)
    {
        this.quoteSets = quoteSets;
    }

    public CountDownLatch getMassQuoteAckLatch()
    {
        return massQuoteAckLatch;
    }

    public void setMassQuoteAckLatch(CountDownLatch massQuoteAckLatch)
    {
        this.massQuoteAckLatch = massQuoteAckLatch;
    }

    public Integer getPriceLevel()
    {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel)
    {
        this.priceLevel = priceLevel;
    }

	public Object getRejectedSecIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
