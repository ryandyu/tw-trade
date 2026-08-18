package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.SecurityIDSource;
import quickfix.fix50.SecurityList;

import com.sumridge.tw.bean.SecurityStatus;
import com.sumridge.tw.bean.TWTrade;

@Component
public class SecurityListProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(SecurityListProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.SecurityList message.");
        
        SecurityList securityList = exchange.getIn().getBody(SecurityList.class);
        
        SecurityList.NoRelatedSym group = new SecurityList.NoRelatedSym();
        
        if (securityList.isSetNoRelatedSym())
        {
            for (int i = 1; i <= securityList.getNoRelatedSym().getValue(); i++)
            {
                securityList.getGroup(i, group);
                
                if (group.isSetSecurityIDSource() && !group.getSecurityIDSource().getValue().equals(SecurityIDSource.CUSIP))
                    continue;
                
                SecurityStatus securityStatus = new SecurityStatus();
                securityStatus.setSecurityId(group.getSecurityID().getValue());
                
                if (group.isSetField(6885))
                    securityStatus.setOfferType(group.getInt(6885));
                
                if (group.isSetBenchmarkSecurityID())
                    securityStatus.setBenchmarkSecurityId(group.getBenchmarkSecurityID().getValue());
                
                this.twTrade.getSecurities().put(securityStatus.getSecurityId(), securityStatus);
            }
        }
    }

}
