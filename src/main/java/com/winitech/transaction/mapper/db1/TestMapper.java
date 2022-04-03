package com.winitech.transaction.mapper.db1;

import com.winitech.transaction.mapper.db1.Db1ConnMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Db1ConnMapper
//@Repository
//@Mapper
public interface TestMapper {

    public void insert(String MBR_ID);

    public List<Map<?, ?>> selectList();

}
