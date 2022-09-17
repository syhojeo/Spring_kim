package hello.core.scan.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        Assertions.assertThat(beanA).isNotNull();

//      ac.getBean("beanB", BeanB.class);
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> ac.getBean("beanB", BeanB.class));
    }


    //Step3. 컴포넌트 스캔의 설정값을 이용하여 필터를 설정한다 (필터의 타입 = Step1에서 만든 어노테이션 적용)
    @Configuration
    @ComponentScan(

            //FilterType.ANNOTATION 어노테이션 타입중 MyIncludeComponent.class 를 포함
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),

            //FilterType.ANNOTATION 어노테이션 타입중 MyExcludeComponent.class 를 제외
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)

            //FilterType.ASSIGNABLE_TYPE 을 사용시 특정 클래스만 집어서 설정가능
            //type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class
            //type = FilterType 사용시 기본값은 ANNOTATION이다 (FilterType = FilterType.ANNOTATION)
    )

    static class ComponentFilterAppConfig {

    }

}
