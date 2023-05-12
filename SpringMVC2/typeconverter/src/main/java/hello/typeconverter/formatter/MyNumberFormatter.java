package hello.typeconverter.formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

/*
    Converter는 객체 -> 객체 컨버팅등 다양한 컨버팅이 가능하다
    하지만 주로 많이 사용하는 컨버팅은
    문자 -> 객체
    객체 -> 문자
    를 사용하는데 이 경우 특별한 컨버터로 Formatter를 사용한다
 */

//Formatter 구현
//Number -> Integer, Long, Float 과 같은 숫자 클래스들의 부모
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    //parse : 문자 -> 객체 변환하는 formatter의 메서드
    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        //"1,000" -> 1000 변환
        //중간에 , 를 숫자에 적용할 경우 자바에서 제공하는 NumberFormat 사용
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    //print : 객체 -> 문자 변환하는 formatter의 메서드
    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        //1000 -> "1,000" 변환
        //중간에 , 를 숫자에 제거할 경우 자바에서 제공하는 NumberFormat 사용
        NumberFormat instance = NumberFormat.getInstance(locale);
        String format = instance.format(object);
        return format;
    }

    //자바에서 제공하는 NumberFormat을 사용하여 숫자에 , 를 적용하는 객체, 문자열에 관한 처리를 쉽게 할 수 있다
}
