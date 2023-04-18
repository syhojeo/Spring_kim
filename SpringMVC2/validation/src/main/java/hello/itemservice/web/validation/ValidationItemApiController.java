package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    HTTP API 로 request 받는 방법에서의 검증 (@RequestBody 를 사용할때 검증 방법)
 */
@Slf4j
@RestController //ResponseBody 자동 추가 해준다 (ResponseBody: 리턴값 message body 넣어 응답하는 방법)
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    //똑같이 @RequestBody와 함께 @Validated 를 넣고, Form 분리 객체, BindingResult를 바로 뒤 순서로 넣어주면 된다
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            //모든 에러를 반환하면 json 으로 에러를 보여준다
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
    /*
        HTTP API 에서 나오는 3가지 경우
        case1 : 생성
        case2 : 입력값 문제로 인한 HTTP Message Converter 가 객체 변환 및 생성을 못한 경우
        - 이 경우에는 Controller 조차 호출이 되지 않는다 HTML Form 과 다르게 HTTP API를 이용한 경우
          바인딩 에러에 대해서는 방법이 없다 그냥 HTTP Message Converter에서 끝나버린다
          (예외처리를 이용해서 에러 메시지 처리를 하는 방법 밖엔 없다)
        case3 : BeanValidation 으로 인한 검증 실패
     */
}
