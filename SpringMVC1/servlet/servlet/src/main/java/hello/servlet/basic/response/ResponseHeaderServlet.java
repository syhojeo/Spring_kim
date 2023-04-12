package hello.servlet.basic.response;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
    HTTP response 의 3요소 status-line, header, message body 에 값을 어떻게 세팅하는지 알아본다
 */
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //1. [status-line]
        //response.setStatus(200);
        response.setStatus(HttpServletResponse.SC_OK); //권장 (200)

        //2. [response-headers]

        //response.setHeader(key, value)
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        //[Header의 편의 메서드] (위와같은 방법보다 좀 더 문법에 맞출 필요없이 메서드를 사용하여 값을 세팅)
        content(response);
        cookie(response);
        redirect(response);

        //3. [message body]

        PrintWriter writer = response.getWriter();
        writer.println("ok");
        writer.println("안녕하세요");
    }

    //편의 메서드 사용법

    private void content(HttpServletResponse response) {

        //Content-Type: text/plain;charset=utf-8
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

        //Content-Length: 2
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    private void cookie(HttpServletResponse response) {

        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); // 600초
        response.addCookie(cookie);

    }

    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html

        /*response.setStatus(HttpServletResponse.SC_FOUND); //302
        response.setHeader("Location", "/basic/hello-form.html");*/
        response.sendRedirect("/basic/hello-form.html");
    }
}
