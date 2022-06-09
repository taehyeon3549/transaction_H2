package com.winitech.transaction.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Db1ConnMapper
public interface TestMapper {

    public void insert(String MBR_ID);

    public List<Map<?, ?>> selectList();


}
