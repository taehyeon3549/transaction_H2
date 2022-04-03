package com.winitech.transaction.mapper.db1;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Db1ConnMapper {
    String value() default "";
}
