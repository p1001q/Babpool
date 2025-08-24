package com.babpool.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.babpool.dto.UserDTO;
import com.babpool.filter.LogFileFilter;

//@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	PrintWriter logWriter = LogFileFilter.getWriter();
        if (logWriter != null) {
            logWriter.println("[LOOUT SUCCESS] 사용자");               
            logWriter.flush();
        }
    	
    	// 세션 끊기
        request.getSession().invalidate();
        
	     // ✅ 자동로그인 쿠키 삭제
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if ("autoLoginEmail".equals(cookie.getName())) {
	                    cookie.setValue(null);
	                    cookie.setMaxAge(0);
	                    cookie.setPath("/");  // 경로 설정 필수 (쿠키 삭제 시)
	                    response.addCookie(cookie);
	                }
	            }
	        }
	        
        // 메인페이지로 이동
        response.sendRedirect(request.getContextPath() + "/mainPage.jsp"); 
    }
}
