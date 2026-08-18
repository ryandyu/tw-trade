package com.sumridge.tw.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.TradeReportID;

public class TradeCaptureReportAckBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(TradeCaptureReportAckBuilder.class);

    private String tradeReportId;

    public TradeCaptureReportAckBuilder tradeReportId(String tradeReportId)
    {
        this.tradeReportId = tradeReportId;
        return this;
    }

    public quickfix.fix50.TradeCaptureReportAck build()
    {
        LOG.debug("Creating quickfix.fix50.TradeCaptureReportAck object.");
        
        quickfix.fix50.TradeCaptureReportAck trdCptRptAck = new quickfix.fix50.TradeCaptureReportAck();
        trdCptRptAck.set(new TradeReportID(this.tradeReportId));
        
        return trdCptRptAck;
    }
}
