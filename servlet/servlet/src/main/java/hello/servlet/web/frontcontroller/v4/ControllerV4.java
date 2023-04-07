package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    /**
     *
     * @param paramMap : request의 parameter 정보 저장 (request.getParameter()에서 나온 정보)
     * @param model : 컨트롤러에서 로직을 수행하고 view 필요한 데이터 저장
     * @return viewName : viewResolver에서 필요한 view의 논리 경로
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);

}
