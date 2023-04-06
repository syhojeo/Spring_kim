package hello.servlet.web.servletmvc;

import java.io.IOException;
import java.net.http.HttpClient;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        //다른 서블릿이나 JSP로 이동할 수 있는 기능. 서버 내부에서 호출이 다시 발생(클라이언트한테 보내는게 아니다)
        dispatcher.forward(request, response);
    }
}
