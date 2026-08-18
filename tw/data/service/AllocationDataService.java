package com.sumridge.tw.data.service;

import com.sumridge.tw.bean.Allocation;

public interface AllocationDataService
{

    public int insert(Allocation allocation);

    public int update(Allocation allocation);

    public void delete(Allocation allocation);

}
