package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	/*
		메시지 빈등록 (스프링부트에선 자동으로 해준다)
	 */
	/*@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new
			ResourceBundleMessageSource();
			//messages 지정했기 때문에 message.properties 파일을 읽어서 사용
			//(국제화는 파일명 마지막에 언어정보를 넣어주면된다) ex) message_en.properties
		messageSource.setBasenames("messages", "errors");
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}*/

}
