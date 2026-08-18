package com.sumridge.tw.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickfix.field.ExecAckStatus;
import quickfix.field.PriceType;
import quickfix.fix50.ExecutionReport;

import com.sumridge.tw.bean.TWTrade;
import com.sumridge.tw.builder.ExecutionAcknowledgementBuilder;
import com.sumridge.tw.builder.QuoteRequestBuilder;

@Component
public class ExecutionReportProcessor implements Processor
{

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionReportProcessor.class);

    @Autowired
    private TWTrade twTrade;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOG.debug("Processing quickfix.fix50.ExecutionReport message.");

        ExecutionReport executionReport = exchange.getIn().getBody(ExecutionReport.class);

        if (!executionReport.isSetField(20111))
        {
            exchange.getIn().setBody(null);
            return;
        }

        double cover = executionReport.getDouble(20111);

        if (executionReport.getPriceType().getValue() == PriceType.SPREAD)
            cover *= 100;
		
		exchange.getIn().setBody(
				new QuoteRequestBuilder()
						.platformId(this.twTrade.getPlatformId())
						.platformRequestId(executionReport.getOrderID().getValue())
						.cover(cover)
						.reasonCode(0)
						.statusCode(99)
						.build()
						);
	}

    public void ack(Exchange exchange) throws Exception
    {
        LOG.debug("Acknowledging quickfix.fix50.ExecutionReport message.");

        ExecutionReport executionReport = exchange.getIn().getBody(ExecutionReport.class);

		exchange.getIn().setBody(
				new ExecutionAcknowledgementBuilder()
						.orderId(executionReport.getOrderID().getValue())
						.clOrdId(executionReport.getClOrdID().getValue())
						.execId(executionReport.getExecID().getValue())
						.transactTime(new LocalDateTime(DateTimeZone.UTC).toDate())
						.symbol(executionReport.getSymbol().getValue())
						.execAckStatus(ExecAckStatus.ACCEPTED)
						.build()
						);
	}
}
