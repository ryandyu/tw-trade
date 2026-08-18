package com.sumridge.tw.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.SecurityListRequestType;
import quickfix.field.SecurityReqID;

public class SecurityListRequestBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(SecurityListRequestBuilder.class);

    private String securityReqId;
    private int securityListRequestType;

    public SecurityListRequestBuilder securityReqId(String securityReqId)
    {
        this.securityReqId = securityReqId;
        return this;
    }

    public SecurityListRequestBuilder securityListRequestType(int securityListRequestType)
    {
        this.securityListRequestType = securityListRequestType;
        return this;
    }

    public quickfix.fix50.SecurityListRequest build()
    {
        LOG.debug("Creating quickfix.fix50.SecurityListRequest message.");
        
        quickfix.fix50.SecurityListRequest securityListRequest = new quickfix.fix50.SecurityListRequest();
        securityListRequest.set(new SecurityReqID(this.securityReqId));
        securityListRequest.set(new SecurityListRequestType(this.securityListRequestType));
        
        return securityListRequest;
    }
}
