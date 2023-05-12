package hello.typeconverter.formatter;

import static org.assertj.core.api.Assertions.*;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

/*
    DefaultFormattingConversionService를 이용한 컨버터 및 포멧터 등록
    - 포멧터를 ConversionService에 등록하기
 */
public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //포멧터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버젼 서비스를 이용한 컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        //컨버젼 서비스를 이용한 포메터 사용
        String convert1 = conversionService.convert(1000, String.class);
        assertThat(convert1).isEqualTo("1,000");

        Long convert2 = conversionService.convert("1,000", Long.class);
        assertThat(convert2).isEqualTo(1000L);

    }
}
