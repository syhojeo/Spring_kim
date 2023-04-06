package hello.servlet.basic.response;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    response로 보내는 응답 메세지 (message body) 는 총 3가지가 있다
    1. 단순 텍스트 응답 (response.getWriter() -> writer.println("ok") 사용)
    2. HTML 응답
    3. HTTP API -Message Body JSON 응답

    이번 예제에서는 HTML 응답에 대해서 알아본다
 */
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //HTML 응답 방법

        //Content-Type: text/html;charset=utf-8 설정
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        //HTML 직접 렌더링
        writer.println("<html>");
        writer.println("<body>");
        writer.println("  <div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
