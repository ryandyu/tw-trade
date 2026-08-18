package com.sumridge.tw.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.tw.data.service.TradePlatformDataService;
import com.sumridge.tw.mapper.TradePlatformMapper;

@Service
public class TradePlatformDataServiceImpl implements TradePlatformDataService
{

    private static final Logger logger = LoggerFactory.getLogger(TradePlatformDataServiceImpl.class);

    @Autowired
    private TradePlatformMapper tradePlatformMapper;

    @Override
    public Integer findTierPriceLevel(Integer id)
    {
        try
        {
            return this.tradePlatformMapper.findTierPriceLevel(id);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
