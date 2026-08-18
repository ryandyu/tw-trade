package com.sumridge.tw.bean;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component("BlockFirmDataMap")
public class BlockFirmDataMap {

	private ConcurrentHashMap<String, BlockFirmData> _cache = new ConcurrentHashMap<String, BlockFirmData>();

	public BlockFirmData get(String account) {
		return _cache.get(account);
	}

	public void put(BlockFirmData data) {
		_cache.put(data.getAccount(), data);
	}
	
}
