package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
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

    //수동 빈 등록과 자동 빈 등록 명이 똑같아서 충돌할떄 수동 빈이 자동빈을 오버라이딩한다
    //그러나 스프링 부트에서는 이것을 원천적으로 차단하여 고쳐야한다
//    @Bean(name = "memoryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }

}
