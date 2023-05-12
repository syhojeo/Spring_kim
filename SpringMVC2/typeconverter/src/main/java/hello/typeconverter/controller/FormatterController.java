package hello.typeconverter.controller;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);
        return "formatter-form";
    }

    /*
        10,000  2023-05-12 14:08:37 두 값이 문자로 들어온다
        문자가 Form 에 컨버팅 되어 저장된다
     */
    @PostMapping("/formatter/edit")
    //@ModelAttribute하면 자동으로 Model 에 Form 이 form 으로 담긴다
    public String formatterEdit(@ModelAttribute Form form) {
        return "formatter-view";
    }

    // 어노테이션을 이용한 Formatting 기능 사용 (스프링에서 기본 제공)
    @Data
    static class Form {

        @NumberFormat(pattern = "###,###")
        private Integer number;

        //해당 패턴을 LocalDateTime으로 바꾸거나 LocalDateTime 형식을 패턴 형식으로 컨버팅 해준다
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }

}
