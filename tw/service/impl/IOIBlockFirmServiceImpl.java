package com.sumridge.tw.service.impl;


import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.sumridge.tw.service.IOIBlockFirmService;

public class IOIBlockFirmServiceImpl implements IOIBlockFirmService {

    private static final Logger logger = LoggerFactory.getLogger(IOIBlockFirmServiceImpl.class);

    @Autowired
    private IOIBlockFirmService ioiBlockFirmService;

    @Override
    public String findBlockFirm(String account)
    {
        try
        {
            return ioiBlockFirmService.findBlockFirm(account);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

}
