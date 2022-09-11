package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//AOP 기능사용
//빈에 등록해야함
@Aspect
@Component
public class TimeTraceAop {

    //AOP를 어디에 적용할지 타게팅
    //                   패키지명, ..밑에있는거, *(모든것)클래스명, (..)파라미터 타입
    //@Around("execution(* hello.hellospring.service..*(..))") -> 서비스 하위만
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
