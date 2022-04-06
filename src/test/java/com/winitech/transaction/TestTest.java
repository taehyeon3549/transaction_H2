package com.winitech.transaction;

import com.winitech.transaction.test.TestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;




@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class TestTest {
    @Autowired
    TestService testService;

    @Test
    public void 테스트(){

        testService.test3().stream().forEach(x -> {
            System.out.println("결과 >>> " + x);
        });

        testService.test33().stream().forEach(x -> {
            System.out.println("결과 >>> " + x);
        });

        Assertions.assertEquals(1, testService.test3().size());
    }
}
