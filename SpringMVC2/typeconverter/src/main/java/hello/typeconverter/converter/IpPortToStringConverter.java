package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
//IpPort 객체를 다시 String으로 변환하는 타입 컨버터
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);
        //IpPort 객체 -> "127.0.0.1:8080"
        return source.getIp() + ":" + source.getPort();
    }
}
