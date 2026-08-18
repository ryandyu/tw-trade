package com.sumridge.tw.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.order.QuoteRequest;
import com.sumridge.xml.jaxb.order.QuoteRequest.Legs;
import com.sumridge.xml.jaxb.order.RequestLegType;

public class QuoteRequestBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRequestBuilder.class);

    private String type;
    private Legs legs;
    private java.util.Date tradeDate;
    private String customerAccount, customerRank, salesPerson, customerRemarks;
    private Integer platformId;
    private String platformRequestId, platformTraderId;
    private String traderAccount;
    private Integer reasonCode;
    private java.util.Date replyByTms, receiveTms;
    private Integer defaultAction;
    private java.util.Date defaultActionTms;
    private int statusCode;
    private java.util.Date goodTillTms, firmTms, spotTms;
    private String listId;
    private int itemCount;
    private String asap, availableActions, allowPartialFill, transactionCode;
    private Double cover;
    private String venue;

    public QuoteRequestBuilder type(String type)
    {
        this.type = type;
        return this;
    }

    public QuoteRequestBuilder legs(Legs legs)
    {
        this.legs = legs;
        return this;
    }

    public QuoteRequestBuilder legs(List<RequestLegType> legTypes)
    {
        Legs legs = XmlJaxbUtils.orderJof.createQuoteRequestLegs();
        legs.getLegs().addAll(legTypes);
        this.legs = legs;
        return this;
    }

    public QuoteRequestBuilder tradeDate(java.util.Date tradeDate)
    {
        this.tradeDate = tradeDate;
        return this;
    }

    public QuoteRequestBuilder customerAccount(String customerAccount)
    {
        this.customerAccount = customerAccount;
        return this;
    }

    public QuoteRequestBuilder customerRank(String customerRank)
    {
        this.customerRank = customerRank;
        return this;
    }

    public QuoteRequestBuilder salesPerson(String salesPerson)
    {
        this.salesPerson = salesPerson;
        return this;
    }

    public QuoteRequestBuilder customerRemarks(String customerRemarks)
    {
        this.customerRemarks = customerRemarks;
        return this;
    }

    public QuoteRequestBuilder platformId(Integer platformId)
    {
        this.platformId = platformId;
        return this;
    }

    public QuoteRequestBuilder platformRequestId(String platformRequestId)
    {
        this.platformRequestId = platformRequestId;
        return this;
    }

    public QuoteRequestBuilder platformTraderId(String platformTraderId)
    {
        this.platformTraderId = platformTraderId;
        return this;
    }

    public QuoteRequestBuilder traderAccount(String traderAccount)
    {
        this.traderAccount = traderAccount;
        return this;
    }

    public QuoteRequestBuilder reasonCode(Integer reasonCode)
    {
        this.reasonCode = reasonCode;
        return this;
    }

    public QuoteRequestBuilder replyByTms(java.util.Date replyByTms)
    {
        this.replyByTms = replyByTms;
        return this;
    }

    public QuoteRequestBuilder receiveTms(java.util.Date receiveTms)
    {
        this.receiveTms = receiveTms;
        return this;
    }

    public QuoteRequestBuilder defaultAction(Integer defaultAction)
    {
        this.defaultAction = defaultAction;
        return this;
    }

    public QuoteRequestBuilder defaultActionTms(java.util.Date defaultActionTms)
    {
        this.defaultActionTms = defaultActionTms;
        return this;
    }

    public QuoteRequestBuilder statusCode(int statusCode)
    {
        this.statusCode = statusCode;
        return this;
    }

    public QuoteRequestBuilder goodTillTms(java.util.Date goodTillTms)
    {
        this.goodTillTms = goodTillTms;
        return this;
    }

    public QuoteRequestBuilder firmTms(java.util.Date firmTms)
    {
        this.firmTms = firmTms;
        return this;
    }

    public QuoteRequestBuilder spotTms(java.util.Date spotTms)
    {
        this.spotTms = spotTms;
        return this;
    }

    public QuoteRequestBuilder listId(String listId)
    {
        this.listId = listId;
        return this;
    }

    public QuoteRequestBuilder itemCount(int itemCount)
    {
        this.itemCount = itemCount;
        return this;
    }

    public QuoteRequestBuilder asap(String asap)
    {
        this.asap = asap;
        return this;
    }

    public QuoteRequestBuilder availableActions(String availableActions)
    {
        this.availableActions = availableActions;
        return this;
    }

    public QuoteRequestBuilder allowPartialFill(String allowPartialFill)
    {
        this.allowPartialFill = allowPartialFill;
        return this;
    }

    public QuoteRequestBuilder transactionCode(String transactionCode)
    {
        this.transactionCode = transactionCode;
        return this;
    }

    public QuoteRequestBuilder cover(Double cover)
    {
        this.cover = cover;
        return this;
    }

    public QuoteRequestBuilder venue(String venue)
    {
        this.venue = venue;
        return this;
    }

    public QuoteRequest build()
    {
        LOG.debug("Creating com.sumridge.xml.jaxb.order.QuoteRequest object.");
        
        QuoteRequest quoteRequest = XmlJaxbUtils.orderJof.createQuoteRequest();
        quoteRequest.setType(this.type);
        quoteRequest.setLegs(this.legs);
        quoteRequest.setTradeDate(this.tradeDate);
        quoteRequest.setCustomerAccount(this.customerAccount);
        quoteRequest.setCustomerRank(this.customerRank);
        quoteRequest.setSalesPerson(this.salesPerson);
        quoteRequest.setCustomerRemarks(this.customerRemarks);
        quoteRequest.setPlatformId(this.platformId);
        quoteRequest.setPlatformRequestId(this.platformRequestId);
        quoteRequest.setPlatformTraderId(this.platformTraderId);
        quoteRequest.setTraderAccount(this.traderAccount);
        quoteRequest.setReasonCode(this.reasonCode);
        quoteRequest.setAsap(this.asap);
        
        if (this.availableActions != null)
            quoteRequest.setAvailableActions(this.availableActions);
        
        quoteRequest.setTransactionCode(this.transactionCode);
        quoteRequest.setAllowPartialFill(this.allowPartialFill);
        quoteRequest.setReplyByTms(this.replyByTms);
        quoteRequest.setReceiveTms(this.receiveTms);
        quoteRequest.setDefaultAction(this.defaultAction);
        quoteRequest.setDefaultActionTms(this.defaultActionTms);
        quoteRequest.setStatusCode(this.statusCode);
        quoteRequest.setGoodTillTms(this.goodTillTms);
        quoteRequest.setFirmTms(this.firmTms);
        quoteRequest.setSpotTms(this.spotTms);
        
        if (this.listId != null)
        {
            quoteRequest.setList(XmlJaxbUtils.orderJof.createListType());
            quoteRequest.getList().setListId(this.listId);
            quoteRequest.getList().setItemCount(this.itemCount);
        }
        
        if (this.cover != null)
            quoteRequest.setCover(this.cover.toString());
        
        quoteRequest.setVenue(this.venue);
        
        return quoteRequest;
    }
}
