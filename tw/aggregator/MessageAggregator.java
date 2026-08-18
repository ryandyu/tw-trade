package com.sumridge.tw.aggregator;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import quickfix.Message;

@Component
public class MessageAggregator implements AggregationStrategy
{

    private static final Logger logger = LoggerFactory.getLogger(MessageAggregator.class);

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
    {
        logger.debug("Aggregating Messages.");

        List<Message> messages = new LinkedList<Message>();

        Message[] newMessages = newExchange.getIn().getBody(Message[].class);

        if (newMessages != null)
        {
            for (Message message : newMessages)
                messages.add(message);
        }

        if (oldExchange == null)
        {
            newExchange.getIn().setBody(messages);
            return newExchange;
        }
        else
        {
            Message[] oldMessages = oldExchange.getIn().getBody(Message[].class);
            if (oldMessages != null)
            {
                for (Message message : oldMessages)
                    messages.add(0, message);
            }
        }

        oldExchange.getIn().setBody(messages);

        return oldExchange;
    }

}
