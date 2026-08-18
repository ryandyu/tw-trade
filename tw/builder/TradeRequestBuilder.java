package com.sumridge.tw.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumridge.xml.jaxb.XmlJaxbUtils;
import com.sumridge.xml.jaxb.order.RequestLegType;
import com.sumridge.xml.jaxb.order.TradeRequest;
import com.sumridge.xml.jaxb.order.TradeRequest.Legs;

public class TradeRequestBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeRequestBuilder.class);

    private Legs legs;
    private String type;
    private Integer platformId;
    private String platformRequestId, platformTraderId;
    private Integer reasonCode;
    private String traderDesk;
    private java.util.Date tradeDate;
    private String customerAccount, salesPerson, customerRemarks;
    private int statusCode;
    private java.util.Date expireTms, spotTms;
    private String listId;
    private int itemCount;
    private int defaultAction;
    private java.util.Date defaultActionTms;
    private String allowPartialFill, transactionCode;
    private String venue;

    public TradeRequestBuilder legs(Legs legs)
    {
        this.legs = legs;
        return this;
    }

    public TradeRequestBuilder legs(List<RequestLegType> legTypes)
    {
        Legs legs = XmlJaxbUtils.orderJof.createTradeRequestLegs();
        legs.getLegs().addAll(legTypes);
        this.legs = legs;
        return this;
    }

    public TradeRequestBuilder type(String type)
    {
        this.type = type;
        return this;
    }

    public TradeRequestBuilder platformId(Integer platformId)
    {
        this.platformId = platformId;
        return this;
    }

    public TradeRequestBuilder platformRequestId(String platformRequestId)
    {
        this.platformRequestId = platformRequestId;
        return this;
    }

    public TradeRequestBuilder platformTraderId(String platformTraderId)
    {
        this.platformTraderId = platformTraderId;
        return this;
    }

    public TradeRequestBuilder reasonCode(Integer reasonCode)
    {
        this.reasonCode = reasonCode;
        return this;
    }

    public TradeRequestBuilder traderDesk(String traderDesk)
    {
        this.traderDesk = traderDesk;
        return this;
    }

    public TradeRequestBuilder tradeDate(java.util.Date tradeDate)
    {
        this.tradeDate = tradeDate;
        return this;
    }

    public TradeRequestBuilder customerAccount(String customerAccount)
    {
        this.customerAccount = customerAccount;
        return this;
    }

    public TradeRequestBuilder salesPerson(String salesPerson)
    {
        this.salesPerson = salesPerson;
        return this;
    }

    public TradeRequestBuilder customerRemarks(String customerRemarks)
    {
        this.customerRemarks = customerRemarks;
        return this;
    }

    public TradeRequestBuilder statusCode(int statusCode)
    {
        this.statusCode = statusCode;
        return this;
    }

    public TradeRequestBuilder expireTms(java.util.Date expireTms)
    {
        this.expireTms = expireTms;
        return this;
    }

    public TradeRequestBuilder spotTms(java.util.Date spotTms)
    {
        this.spotTms = spotTms;
        return this;
    }

    public TradeRequestBuilder listId(String listId)
    {
        this.listId = listId;
        return this;
    }

    public TradeRequestBuilder itemCount(int itemCount)
    {
        this.itemCount = itemCount;
        return this;
    }

    public TradeRequestBuilder defaultAction(int defaultAction)
    {
        this.defaultAction = defaultAction;
        return this;
    }

    public TradeRequestBuilder defaultActionTms(java.util.Date defaultActionTms)
    {
        this.defaultActionTms = defaultActionTms;
        return this;
    }

    public TradeRequestBuilder allowPartialFill(String allowPartialFill)
    {
        this.allowPartialFill = allowPartialFill;
        return this;
    }

    public TradeRequestBuilder transactionCode(String transactionCode)
    {
        this.transactionCode = transactionCode;
        return this;
    }

    public TradeRequestBuilder venue(String venue)
    {
        this.venue = venue;
        return this;
    }

    public com.sumridge.xml.jaxb.order.TradeRequest build()
    {
        LOG.debug("Creating com.sumridge.xml.jaxb.order.TradeRequest object.");
        
        TradeRequest tradeRequest = XmlJaxbUtils.orderJof.createTradeRequest();
        tradeRequest.setLegs(this.legs);
        tradeRequest.setType(this.type);
        tradeRequest.setPlatformId(this.platformId);
        tradeRequest.setPlatformRequestId(this.platformRequestId);
        tradeRequest.setPlatformTraderId(this.platformTraderId);
        tradeRequest.setReasonCode(this.reasonCode);
        tradeRequest.setTraderDesk(this.traderDesk);
        tradeRequest.setTradeDate(this.tradeDate);
        tradeRequest.setCustomerAccount(this.customerAccount);
        tradeRequest.setSalesPerson(this.salesPerson);
        tradeRequest.setCustomerRemarks(this.customerRemarks);
        tradeRequest.setTransactionCode(this.transactionCode);
        tradeRequest.setAllowPartialFill(this.allowPartialFill);
        tradeRequest.setStatusCode(this.statusCode);
        tradeRequest.setExpireTms(this.expireTms);
        tradeRequest.setSpotTms(this.spotTms);
        
        if (this.listId != null)
        {
            tradeRequest.setList(XmlJaxbUtils.orderJof.createListType());
            tradeRequest.getList().setListId(this.listId);
            tradeRequest.getList().setItemCount(this.itemCount);
        }
        
        tradeRequest.setDefaultAction(this.defaultAction);
        tradeRequest.setDefaultActionTms(this.defaultActionTms);
        tradeRequest.setVenue(this.venue);
        
        return tradeRequest;
    }
}
