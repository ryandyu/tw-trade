package com.sumridge.tw.bean;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;


@Component
public class TWebRefDataQuery {
	   private Logger logger = LoggerFactory.getLogger(getClass());

	    @Autowired
	    private SqlMapClientImpl sqlMapClientTemplate;

	    @Autowired
	    private BlockFirmDataMap  maps;
	    
	    @SuppressWarnings("unchecked")    
	    public void loadRef()
	    {            	
	    	try {
		    	String query = "blockfirmDataSQL.select-blockfirm";
	
		        List<BlockFirmData> list = sqlMapClientTemplate.queryForList(query);
	
		        for (BlockFirmData data : list)  {
		        	maps.put(data);
		        }		        
		        logger.info("Loaded Tweb block firm data, number of row = " + list.size());
		        
	    	} catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }

	    
	    public String getBlockFirmByAccount(final String account)  {
	    	return maps.get(account).getFirm(); 
	    }
}
