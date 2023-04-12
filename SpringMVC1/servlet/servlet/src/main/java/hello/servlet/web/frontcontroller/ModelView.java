package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;

/*
    ModelView - viewResolver에서 필요한 viewName(view논리경로)와 request Attribute에 저장하던 정보 model를 가진 객체
    - view에서 필요한 정보들을 저장하는 객체이다

    기존에는 request.setAttribute를 통해 정보를 담아서 JSP와 같은 view단으로 값을 보내줬다
    하지만 frontController의 사용으로 실제 controller 로직에서는
    HttpServletRequest, HttpServletResponse를 굳이 보낼 필요가 없다 반복되는 코드가 된다
    때문에 request.setAttribute와 같이 값을 전달할 상자가 대신 필요한데
    이상자가 바로 Model이다
    model의 형태는 Map<String, Object>로 되어있으며 이는 request의 Attribute와 똑같은 형태를 갖는다
    ex) request.setAttribute("username", "abc); request.setAttribute("age", 3);

    view 또한 viewResolver를 통해서 전체경로(WEB-INF/view/new-form.jsp)를 만들게되는데
    Controller에서 viewResolver에 논리경로(new-form)을 넣어두고 프론트 컨트롤러로 ModelView를 반환해
    viewResolver에서 이 ModelView를 통해서 전체경로 설정을 할 수 있도록 정보를 전달한다
 */
public class ModelView {

    //실제
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
