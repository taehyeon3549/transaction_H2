package com.winitech.transaction.test;

import com.winitech.transaction.mapper.SetTestMapper2;
import com.winitech.transaction.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestService {
    final TestMapper testMapper;

    final SetTestMapper2 setTestMapper2;


    /**
     * CheckedException
     *
     * >> rollback 동작 안함
     * */
    @Transactional(value = "db1TransactionManager", rollbackFor = Exception.class)
    public String test(){

        try {
            testMapper.insert("FRIST");
            testMapper.insert("FRIST");     // PK 중복 exception 발생
            testMapper.insert("THIRD");

            return testMapper.selectList().toString();

        }catch (Exception e){
            log.error("에러발생 >>> {}",e.toString());
            return "fail";
        }

    }


    @Transactional(value = "db1TransactionManager", rollbackFor = Exception.class)
    public String test2()throws Exception{

        testMapper.insert("FRIST");
//        testMapper.insert("FRIST");     // PK 중복 exception 발생
        testMapper.insert("THIRD");

        return testMapper.selectList().toString();

    }


    public String test4(){
        return testMapper.selectList().toString();
    }


    public List<Map<?,?>> test3(){
        return testMapper.selectList();
    }

    public List<Map<?,?>> test33(){
        return setTestMapper2.selectList();
    }

//    public String test11(){
//        return db3SqlSession
//                .selectList("com.winitech.transaction.mapper.TestMapper.selectList")
//                .toString();
//    }
}
