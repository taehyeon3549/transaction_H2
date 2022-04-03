package com.winitech.transaction.test;

import com.winitech.transaction.mapper.db1.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * TestController.
 *
 * <p>
 *     'Controller에서 Transactional 처리를 못하는가?'
 *     에 대한 궁금증에 확인 하기 위해 테스트 진행
 *
 *     결론 > 가능 하다.
 *
 *
 *      - 스프링에서는 @Transactional을 사용한 Checked Exception은 롤백되지 않습니다.
 *      - 이유는 스프링이 EJB에서의 관습을 따르기 때문이라고 합니다.
 * </p>
 *
 * @author CS 김태현
 * @version 1.0.0
 * @since 2022-04-01
 * @modify
 * @see "https://annajinee.tistory.com/64" : checked Exception 과 unchecked Exception 설명
 * @see "https://goddaehee.tistory.com/167" : 갓대희 @Transaction 설명
 *
 * <p>
 *  수정일      수정자      수정내용<br>
 *  ----------  --------   ---------------------------<br>
 *  2022-04-01  CS 김태현   최초작성 <br>
 * </p>
 *
 * @see
**/
@RestController
@RequestMapping("/test")
@Log4j2
public class TestController {
//    @Resource   SqlSession defaultSqlSession;
    @Autowired
    private TestService testService;

    @Autowired
    private TestMapper testMapper;

    /**
     * sqlSession을 통해 호출 후 실패시 DataSourceTransactionManager 를 통해 rollback 실행
     *
     * rollback 됨
     * */
  /*  @GetMapping("/testing")
    @Transactional(value = "defaultTransactionManager")
    public String testing(){

        try {
            defaultSqlSession.insert("A", "FRIST");
            defaultSqlSession.insert("A", "FRIST");
            defaultSqlSession.insert("A", "THIRD");

            return defaultSqlSession.selectList("test").toString();

        }catch (Exception e){
            log.error(e.toString());

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return "fail";
        }
    }*/

    /**
     * Mapper를 통해 호출 후 실패시 DataSourceTransactionManager 를 통해 rollback 실행
     *
     * rollback 됨
     * */
    @GetMapping("/testing2")
    @Transactional(value = "defaultTransactionManager")
    public String testing2(){

        try {
            testMapper.insert("FRIST");
            testMapper.insert("FRIST");     // PK 중복 exception 발생
            testMapper.insert("THIRD");

            return testMapper.selectList().toString();

        }catch (Exception e){
            log.error("에러발생 >>> {}",e.toString());

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return "fail";
        }

    }

    /**
     * rollback 안됨
     * */
    @GetMapping("/testing3")
    public String testing3(){
        return testService.test();
    }

    /**
     * rollback 정상
     * */
    @GetMapping("/testing4")
    public String testing4(){
        try {
            return testService.test2();
        }catch (Exception e){
            log.error("에러발생 >>> {}",e.toString());
            return "fail";
        }
    }

    @GetMapping("/testing5")
    @Transactional(value = "db1TransactionManager")
    public String testing5()throws Exception{
        testMapper.insert("FRIST");
        testMapper.insert("FRIST");
        testMapper.insert(testMapper.selectList().get(0).get("MBR_ID").toString());

        return testMapper.selectList().toString();
    }

    @GetMapping("/testing6")
    @Transactional
    public String testing6(){

        try {
            testMapper.insert("FRIST");
            testMapper.insert("FRIST");
            testMapper.insert(testMapper.selectList().get(0).get("MBR_ID").toString());

            return testMapper.selectList().toString();
        }catch (Exception e){
            log.error("에러발생 >>> {}",e.toString());

            return "fail";
        }
    }
}
