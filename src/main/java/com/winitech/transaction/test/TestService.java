package com.winitech.transaction.test;

import com.winitech.transaction.mapper.db1.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TestService {
    final TestMapper testMapper;

    /**
     * CheckedException
     *
     * >> rollback 동작 안함
     * */
    @Transactional(rollbackFor = Exception.class)
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


    @Transactional(rollbackFor = Exception.class)
    public String test2()throws Exception{

        testMapper.insert("FRIST");
        testMapper.insert("FRIST");     // PK 중복 exception 발생
        testMapper.insert("THIRD");

        return testMapper.selectList().toString();

    }
}
