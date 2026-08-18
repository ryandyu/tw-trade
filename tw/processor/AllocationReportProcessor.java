package com.sumridge.tw.processor;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.fix50.AllocationReport;

import com.sumridge.tw.bean.Allocation;
import com.sumridge.tw.bean.Allocation.AllocationBuilder;
import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.AllocationReportAckBuilder;

@Component
public class AllocationReportProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(AllocationReportProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.AllocationReport message.");
        
        final AllocationReport allocationReport = exchange.getIn().getBody(AllocationReport.class);
        
        List<Allocation> allocations = new LinkedList<Allocation>();
        
        if (allocationReport.isSetNoOrders())
        {
            AllocationReport.NoOrders group = new AllocationReport.NoOrders();
            for (int i = 1; i <= allocationReport.getNoOrders().getValue(); i++)
            {
                allocationReport.getGroup(i, group);
                
                if (!group.isSetClOrdID())
                    continue;
                
                allocations.add(new AllocationBuilder()
                                .tradeDate(DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(allocationReport.getTradeDate().getValue()).toDate())
                                .platformId(this.twTrade.getPlatformId())
                                .execId(allocationReport.isSetField(6731) ? allocationReport.getString(6731) : "")
								.orderId("0")
								.clOrdId("0")
								.allocId(new Integer(i).toString())
								.allocStatus(allocationReport.getAllocStatus().getValue())
								.side(String.valueOf(allocationReport.getSide().getValue()))
								.allocTransType(allocationReport.getAllocTransType().getValue())
								.build()
								);
			}
		}
        
        for (int i = 0; i < allocations.size(); i++)
        {
            Allocation allocation = allocations.get(i);

            if (allocationReport.isSetNoAllocs())
            {
                AllocationReport.NoAllocs noAllocs = new AllocationReport.NoAllocs();
                for (int j = 1; j <= allocationReport.getNoAllocs().getValue(); j++)
                {
                    if (i != j - 1)
                        continue;
			
                    allocationReport.getGroup(j, noAllocs);

                    if (noAllocs.isSetAllocAccount())
                    {
                        allocation.setAlertCode(noAllocs.getAllocAccount().getValue());
                        allocation.setShortName(noAllocs.getAllocAccount().getValue());
                        allocation.setClientId(noAllocs.getAllocAccount().getValue());
                    }

                    if (noAllocs.isSetAllocQty())
                        allocation.setRealSize((int) (1000 * noAllocs.getAllocQty().getValue()));
					
                    if (noAllocs.isSetAllocNetMoney())
                        allocation.setNetMoney(noAllocs.getAllocNetMoney().getValue());

                    if (noAllocs.isSetAllocAccruedInterestAmt())
                        allocation.setAccruedInterest(noAllocs.getAllocAccruedInterestAmt().getValue());

                    if (allocation.getNetMoney() != null || allocation.getAccruedInterest() != null)
                        allocation.setPrincipal((allocation.getNetMoney() != null ? allocation.getNetMoney() : 0d)
                                - (allocation.getAccruedInterest() != null ? allocation.getAccruedInterest() : 0d));

				}
			}
            
            if (allocationReport.isSetNoPartyIDs())
            {
                AllocationReport.NoPartyIDs noPartyIds = new AllocationReport.NoPartyIDs();
               
                for (int j = 1; j <= allocationReport.getNoPartyIDs().getValue(); j++)
                {
                    allocationReport.getGroup(j, noPartyIds);
                    
                    if (noPartyIds.isSetPartyRole())
                    {
                        switch (noPartyIds.getPartyRole().getValue())
                        {
                            case PartyRole.CLIENT_ID:
                                if (noPartyIds.isSetPartyIDSource()
                                        && noPartyIds.getPartyIDSource().getValue() == PartyIDSource.GENERALLY_ACCEPTED_MARKET_PARTICIPANT_IDENTIFIER)
                                    allocation.setCustomer(StringUtils.substring(noPartyIds.getPartyID().getValue(), 0, 32));
                                break;
                                
                            default:
                                break;
                        }
                    }
                }
            }
        }
        
		exchange.getIn().setBody(allocations);
	}

    public void acknowledge(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.AllocationReport message.");

        final AllocationReport allocationReport = exchange.getIn().getBody(AllocationReport.class);

        exchange.getIn().setBody(
                new AllocationReportAckBuilder()
                .allocReportID(allocationReport.isSetAllocReportID() ? allocationReport.getAllocReportID().getValue() : "")
                .build()
                );
	}
}
