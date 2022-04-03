package com.winitech.transaction.mapper.db2;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Db2ConnMapper {
    String value() default "";
}
