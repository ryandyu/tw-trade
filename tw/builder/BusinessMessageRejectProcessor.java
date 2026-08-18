package com.sumridge.tw.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumridge.tw.bean.TWTrade;

import quickfix.fix50.BusinessMessageReject;
import quickfix.fix50.IOI;

@Component
public class BusinessMessageRejectProcessor implements Processor
{

    @Autowired
    private TWTrade twTrade;
    
    @Override
    public void process(Exchange exchange) throws Exception
    {
        BusinessMessageReject rej = exchange.getIn().getBody(BusinessMessageReject.class);
        
        if(IOI.MSGTYPE.equals(rej.getRefMsgType().getValue()) && rej.isSetBusinessRejectRefID())
        {
            String refId = rej.getBusinessRejectRefID().getValue();
            
//            twTrade.getRejectedSecIds().add(refId.split("_")[0]);
        }
    }

}
