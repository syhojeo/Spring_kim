package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
        return "converter-view";
    }

    /*
        Form 객체를 만들어 @ModelAttribute 사용할 수 있도록 한다
        Form 은 @Data를 통해 getter setter가 모두 있으며
        @ModelAttribute 사용시 Form 의 프로퍼티 ipPort 의 setter를 호출하여
        HTML FORM 의 값을 알맞게 넣는다
        이때 HTML FORM 에서 제출하는 ipPort가 매핑되며
        ipPort는 문자열(127.0.0.1:8080)으로 submit 되지만
        Spring의 타입 컨버팅이 사용되어 ipPort 객체 내부의 ip(127.0.0.1), Port(8080)으로 알맞게 컨버팅되어
        객체 내부에 저장된다
     */

    /*
        Spring -> viewTemplate 컨버팅 변환
        Form 객체에 ipPort를 넣고 viewTemplate 에 전달
        view 에서는 thymeleaf의 object를 이용하여 form 을 사용하고
        field를 이용하여 ipPort의 ip, port 프로퍼티를 문자열로 자동 컨버팅하여 사용한다
     */
    @GetMapping("/converter/edit")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.7", 8080);
        Form form = new Form(ipPort);
        model.addAttribute("form", form);
        return "converter-form";
    }

    /*
        HTML Form -> Spring 시 컨버팅 변환
        Form 객체를 만들어 @ModelAttribute 사용할 수 있도록 한다
        Form 은 @Data를 통해 getter setter가 모두 있으며
        @ModelAttribute 사용시 Form 의 프로퍼티 ipPort 의 setter를 호출하여
        HTML FORM 의 값을 알맞게 넣는다
        이때 HTML FORM 에서 제출하는 ipPort가 매핑되며
        ipPort는 문자열(127.0.0.1:8080)으로 submit 되지만
        Spring의 타입 컨버팅이 사용되어 ipPort 객체 내부의 ip(127.0.0.1), Port(8080)으로 알맞게 컨버팅되어
        객체 내부에 저장된다
     */
    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {
        IpPort ipPort = form.getIpPort();
        model.addAttribute("ipPort", ipPort);
        return "converter-view";
    }

    @Data
    static class Form {

        private IpPort ipPort;

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }

}
