package com.sumridge.tw.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.tw.bean.Allocation;
import com.sumridge.tw.data.service.AllocationDataService;
import com.sumridge.tw.mapper.AllocationMapper;

@Service
public class AllocationDataServiceImpl implements AllocationDataService
{

    private static final Logger logger = LoggerFactory.getLogger(AllocationDataServiceImpl.class);

    @Autowired
    private AllocationMapper allocationMapper;

    @Override
    public int insert(Allocation allocation)
    {
        try
        {
            return allocationMapper.insert(allocation);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Allocation allocation)
    {
        try
        {
            return allocationMapper.update(allocation);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Allocation allocation)
    {
        try
        {
            allocationMapper.delete(allocation);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
