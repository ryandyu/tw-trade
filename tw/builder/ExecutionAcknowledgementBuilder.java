package com.sumridge.tw.builder;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.ClOrdID;
import quickfix.field.DKReason;
import quickfix.field.ExecAckStatus;
import quickfix.field.ExecID;
import quickfix.field.OrderID;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class ExecutionAcknowledgementBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionAcknowledgementBuilder.class);

    private String orderId;
    private String clOrdId;
    private String execId;
    private java.util.Date transactTime;
    private String symbol;
    private char execAckStatus;
    private char dkReason;
    private String text;

    public ExecutionAcknowledgementBuilder orderId(String orderId)
    {
        this.orderId = orderId;
        return this;
    }

    public ExecutionAcknowledgementBuilder clOrdId(String clOrdId)
    {
        this.clOrdId = clOrdId;
        return this;
    }

    public ExecutionAcknowledgementBuilder execId(String execId)
    {
        this.execId = execId;
        return this;
    }

    public ExecutionAcknowledgementBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public ExecutionAcknowledgementBuilder symbol(String symbol)
    {
        this.symbol = symbol;
        return this;
    }

    public ExecutionAcknowledgementBuilder execAckStatus(char execAckStatus)
    {
        this.execAckStatus = execAckStatus;
        return this;
    }

    public ExecutionAcknowledgementBuilder dkReason(char dkReason)
    {
        this.dkReason = dkReason;
        return this;
    }

    public ExecutionAcknowledgementBuilder text(String text)
    {
        this.text = text;
        return this;
    }

	public quickfix.fix50.ExecutionAcknowledgement build()
	{
		LOG.debug("Creating quickfix.fix50.ExecutionAcknowledgement object.");
		
		quickfix.fix50.ExecutionAcknowledgement executionAck = new quickfix.fix50.ExecutionAcknowledgement();

        if (this.orderId != null)
            executionAck.set(new OrderID(this.orderId));
        
        if (this.clOrdId != null)
            executionAck.set(new ClOrdID(this.clOrdId));
        
        executionAck.set(new ExecID(this.execId));
        executionAck.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        executionAck.set(new Symbol(this.symbol));
        executionAck.set(new ExecAckStatus(this.execAckStatus));
        
        if (this.execAckStatus == ExecAckStatus.DONT_KNOW)
        {
            executionAck.set(new DKReason(this.dkReason));
            executionAck.set(new Text(this.text));
        }
		
		return executionAck;
	}

}
