package com.sumridge.tw.data.service.impl;

import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumridge.tw.bean.TWebBlockFirm;
import com.sumridge.tw.data.service.IOIBlockFirmDataService;
import com.sumridge.tw.mapper.IOIBlockFirmMapper;

@Service
public class IOIBlockFirmDataServiceImpl implements IOIBlockFirmDataService {

    private static final Logger logger = LoggerFactory.getLogger(IOIBlockFirmDataServiceImpl.class);

    @Autowired
    private IOIBlockFirmMapper ioiBlockFirmService;

    @Override
    public String findBlockFirm(String account)
    {
        try
        {
        	TWebBlockFirm data = ioiBlockFirmService.findBlockFirm(account);
        	return data.isOnOff() == true ?  data.getBlockFirm() : "" ;       	
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }

}
