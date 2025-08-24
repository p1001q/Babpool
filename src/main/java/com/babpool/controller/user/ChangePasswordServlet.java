package com.babpool.controller.user;

import com.babpool.dao.UserDAO;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;
import com.babpool.utils.SHA256Util;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션에서 이메일 가져오기
//        String email = (String) request.getSession().getAttribute("email");
        // 동국 -  myPage Session에서 유지 중인 UserDTO 를 통해서 Email 뽑기
    	String email = (String) ((com.babpool.dto.UserDTO) request.getSession().getAttribute("loginUser")).getEmail();
        
        // 이메일이 없다면 로그인 페이지로 리디렉션
        if (email == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 새로운 비밀번호와 확인 비밀번호 가져오기
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 비밀번호가 일치하지 않으면 오류 메시지
        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("changePassword.jsp?error=passwordsNotMatch");
            return;
        }

        // SHA-256 해싱
        String hashedPassword = SHA256Util.encrypt(newPassword);

        // 데이터베이스 연결 및 비밀번호 업데이트
        try (Connection conn = DBUtil.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);

            // 비밀번호 업데이트 성공 여부
            boolean isUpdated = userDAO.updatePasswordByEmail(email, hashedPassword);

            if (isUpdated) {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[CHANGE SUCCESS] 사용자: " + email);
                    logWriter.flush();
                }
            	
                response.sendRedirect("myPage.jsp?message=passwordUpdated");
            } else {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[CHANGE FAIL] 사용자: " + email);
                    logWriter.flush();
                }
                response.sendRedirect("changePassword.jsp?error=updateFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("changePassword.jsp?error=serverError");
        }
    }
}