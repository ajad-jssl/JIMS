package com.JIMS.integration.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = 500;
        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
        }

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return simpleErrorPage("404", "Page Not Found", "Something went wrong.");
        }

        return simpleErrorPage("500", "Page Not Found", "Something went wrong.");
    }

    private String simpleErrorPage(String code, String title, String message) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "  <title>Error " + code + "</title>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "  <style>"
                + "    body {"
                + "      margin:0;"
                + "      font-family: 'Segoe UI', Arial, sans-serif;"
                + "     background: radial-gradient(circle,rgba(255, 255, 255, 1) 0%, rgba(199, 223, 242, 1) 50%);"
                + "      height:100vh;"
                + "      display:flex;"
                + "      justify-content:center;"
                + "      align-items:center;"
                + "      color:#333;"
                + "    }"
                + "    .container {"
                + "      background:#fff;"
                + "      padding:40px;"
                + "      border-radius:12px;"
                + "      text-align:center;"
                + "      box-shadow:0 10px 30px rgba(0,0,0,0.2);"
                + "      max-width:400px;"
                + "      width:90%;"
                + "    }"
                + "    .code {"
                + "      font-size:80px;"
                + "      font-weight:bold;"
                + "      color:#764ba2;"
                + "      margin-bottom:10px;"
                + "    }"
                + "    .title {"
                + "      font-size:22px;"
                + "      margin-bottom:10px;"
                + "    }"
                + "    .message {"
                + "      font-size:14px;"
                + "      color:#666;"
                + "      margin-bottom:20px;"
                + "    }"
                + "    .btn {"
                + "      display:inline-block;"
                + "      padding:10px 20px;"
                + "      background:#667eea;"
                + "      color:#fff;"
                + "      text-decoration:none;"
                + "      border-radius:6px;"
                + "      transition:0.3s;"
                + "    }"
                + "    .btn:hover {"
                + "      background:#5a67d8;"
                + "    }"
                + "  </style>"
                + "</head>"
                + "<body>"
                + "  <div class='container'>"
                + "    <div class='code'>" + code + "</div>"
                + "    <div class='title'>" + title + "</div>"
                + "    <div class='message'>" + message + "</div>"
                
                + "  </div>"
                + "</body>"
                + "</html>";
    }
}