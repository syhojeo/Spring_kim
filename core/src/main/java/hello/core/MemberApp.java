package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
        //appconfig에서 memberService를  가져온다 (DI)
//        MemberService memberService = appConfig.memberService();
        //MemberServiceImpl memberService = new MemberServiceImpl();

        //Spring 사용
        // ApplicationContext : 스프링 컨테이너
        // AnnotationConfigApplicationContext(설정정보 (AppConfig.class)) -> Annotation Config 기반의 스프링컨테이너를 만들어라
        // 1. 스프링 컨테이너는 AppConfig를 설정 정보로 사용
        // 2. AppConfig에서 @Bean이라 붙은 메서드들을 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록
        // 3. 스프링 컨테이너에 등록된 객체는 스프링 빈 이라 불리며 각 빈의 이름은 @Bean 이 붙은 메서드의 명을 사용한다
        //ApplicationContext.getBean(AppConfig에 정의된 method명, 메서드가 가져오는 반환타입)
        // 4. getBean을 통해 스프링 빈을 가져와서 사용한다

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);

        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
