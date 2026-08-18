package com.sumridge.tw.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.AllocReportID;

public class AllocationReportAckBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(AllocationReportAckBuilder.class);

    private String allocReportID;

    public AllocationReportAckBuilder allocReportID(String allocReportID)
    {
        this.allocReportID = allocReportID;
        return this;
    }

    public quickfix.fix50.AllocationReportAck build()
    {
        LOG.debug("Creating quickfix.fix50.AllocationReportAck object.");
        
        quickfix.fix50.AllocationReportAck allocRptAck = new quickfix.fix50.AllocationReportAck();
        allocRptAck.set(new AllocReportID(this.allocReportID));
        
        return allocRptAck;
    }
}
