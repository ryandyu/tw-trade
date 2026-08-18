package com.sumridge.tw.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.tw.data.service.TradePlatformDataService;
import com.sumridge.tw.service.TradePlatformService;

@Service
public class TradePlatformServiceImpl implements TradePlatformService
{

    private static final Logger logger = LoggerFactory.getLogger(TradePlatformServiceImpl.class);

    @Autowired
    private TradePlatformDataService tradePlatformDataService;

    @Override
    public Integer findTierPriceLevel(Integer id)
    {
        try
        {
            return this.tradePlatformDataService.findTierPriceLevel(id);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
