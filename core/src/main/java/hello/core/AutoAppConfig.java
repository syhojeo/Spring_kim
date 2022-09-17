package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(

        //basePackages를 이용하여 ComponentScan 범위 설정
        basePackages = "hello.core.member", //hello.core.member 만 ComponentScan의 Scan 범위 대상

        //해당 클래스가 위치하는 패키지 내부에서만 검색
        basePackageClasses = AutoAppConfig.class, // AutoAppConfig.class 의 패키지 = hello.core 에서만 검색

        // 아무런 지정없이 Default로 할 경우 @ComponentScan이 붙은 설정 정보 클래스의 패키지와 하위패키지를 모두 스캔한다
        // 때문에 어플리케이션의 가장 상위 패키지 위치에 @ComponentScan 설정 파일을 놓고 사용하는것을 권장한다

        //다른 설정(AppConfig)와 같은것을 읽어오지 않기 위해 Filter를 사용한다
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}
