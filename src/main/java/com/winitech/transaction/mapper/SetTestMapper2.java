package com.winitech.transaction.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Db2ConnMapper
public interface SetTestMapper2 {

    public void insert(String MBR_ID);

    public List<Map<?, ?>> selectList();

}
