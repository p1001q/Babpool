package com.babpool.filter;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebFilter("/*")  // 모든 요청에 적용
public class AutoLoginFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);  // 기존 세션 확인 (새로 생성 안함)

        if (session == null || session.getAttribute("loginUser") == null) {
            // 세션이 없는 경우 → 쿠키 확인
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("autoLoginEmail".equals(cookie.getName())) {
                        String email = cookie.getValue();

                        try (Connection conn = DBUtil.getConnection()) {
                            UserDAO userDAO = new UserDAO(conn);
                            UserDTO user = userDAO.getUserByEmail(email);
                            if (user != null) {
                                session = httpRequest.getSession();  // 세션 새로 생성
                                session.setAttribute("loginUser", user);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);  // 다음 필터 or 서블릿으로 넘김
    }
}
