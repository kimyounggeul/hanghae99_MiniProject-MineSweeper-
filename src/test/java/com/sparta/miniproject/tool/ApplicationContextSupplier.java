package com.sparta.miniproject.tool;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(MockController.class)
@EnableJwtAuthorizationFilterTest
@EnableGlobalExceptionControllerAdviceTest
public @interface ApplicationContextSupplier {
}
