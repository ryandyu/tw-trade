package com.sumridge.tw.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main
{

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private org.apache.camel.spring.Main main;

    public static void main(String[] args) throws Exception
    {
        new Main().boot(args);
    }

    public void boot(String[] args) throws Exception
    {
        final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("tw-trade.xml");
        main = new org.apache.camel.spring.Main();
        main.setApplicationContext(ctx);
       
        LOG.info("Starting Camel context");
        main.run(args);
    }

}
