package hello.core.scan.filter;

import java.lang.annotation.*;

//Step1. 필터를 적용할 기준인 어노테이션 만들기

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {

}
