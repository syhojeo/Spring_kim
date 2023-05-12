package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //WebConfig에서 Converter 등록하기
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //밑의 작업을 통해 스프링 내부에서 사용하는 ConversionService에 Converter를 등록한다
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //Formatter 의 우선순위가 converter보다 낮기 때문에 MyNumberFormatter와의 중복 타입으로 인해 주석처리
        //registry.addConverter(new StringToIntegerConverter());
        //registry.addConverter(new IntegerToStringConverter());

        //추가 addFormatter
        registry.addFormatter(new MyNumberFormatter());
    }
}
