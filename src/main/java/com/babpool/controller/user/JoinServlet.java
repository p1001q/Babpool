package com.babpool.controller.user;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;
import com.babpool.utils.SHA256Util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        // - 동국 06/14 입력부 수정
        String password = request.getParameter("password")
        		.replaceAll("[^\\p{Print}]", "")
        	    .trim();

        // 비밀번호 SHA-256 해싱
        String hashedPassword = SHA256Util.encrypt(password);

        // DTO 객체에 값 설정
        UserDTO user = new UserDTO();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setProfileImagePath("/resource/images/default_profile.png");  // ⭐ 수연 추가

        // DB 저장 시도
        try (Connection conn = DBUtil.getConnection()) {
            UserDAO dao = new UserDAO(conn);
            boolean isInserted = dao.insertUser(user);

            if (isInserted) {
                
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[JOIN SUCCESS] 사용자 : " + email);
                    logWriter.flush();
                }
            	
            	response.sendRedirect("login.jsp");
                
            } else {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[JOIN FAIL] 사용자 : " + email);
                    logWriter.flush();
                }
                response.sendRedirect("join.jsp?error=insertFail");
            }
        } catch (Exception e) {
            e.printStackTrace();  // 나중에 로그파일로 대체 가능
            response.sendRedirect("join.jsp?error=serverError");
        }
    }
}