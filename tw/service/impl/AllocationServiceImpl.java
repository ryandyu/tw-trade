package com.sumridge.tw.service.impl;

import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.tw.bean.Allocation;
import com.sumridge.tw.data.service.AllocationDataService;
import com.sumridge.tw.service.AllocationService;

@Service
public class AllocationServiceImpl implements AllocationService
{

    private static final Logger logger = LoggerFactory.getLogger(AllocationServiceImpl.class);

    @Autowired
    private AllocationDataService allocationDataService;

    @Override
    public void insert(Allocation allocation)
    {
        try
        {
            this.allocationDataService.insert(allocation);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

    @Override
    public void update(Allocation allocation)
    {
        try
        {
            int retVal = this.allocationDataService.update(allocation);
            if (retVal < 1)
            {
                logger.warn("missing row for Allocation update.");
                this.allocationDataService.insert(allocation);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

    @Override
    public void delete(Allocation allocation)
    {
        try
        {
            this.allocationDataService.delete(allocation);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

}
