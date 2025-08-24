package com.babpool.controller.user;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

// 동국 - mypage - 탈퇴버튼
//@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션에서 로그인된 사용자 이메일 가져오기
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        // 로그인된 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // DB에서 사용자 삭제 작업
        Connection conn = null;
        UserDAO userDAO = null;

        try {
            conn = DBUtil.getConnection();  // DB 연결
            userDAO = new UserDAO(conn);

            // 이메일로 사용자 삭제
            boolean deleteSuccess = userDAO.deleteUserByEmail(loginUser.getEmail());

            // 삭제 성공하면 세션에서 로그아웃 처리 후 로그인 페이지로 리다이렉트
            if (deleteSuccess) {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[DELETE SUCCESS] 사용자 : " + loginUser);
                    logWriter.flush();
                }
            	
                session.invalidate();  // 세션 무효화
                response.sendRedirect("mainPage.jsp");  // mainPage.jsp로 리다이렉트
            } else {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[DELETE FAIL] 사용자: " + loginUser);
                    logWriter.flush();
                }
            	
                request.setAttribute("errorMessage", "사용자 삭제에 실패했습니다.");
                request.getRequestDispatcher("mypage.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "탈퇴 처리 중 오류가 발생했습니다.");
            request.getRequestDispatcher("mypage.jsp").forward(request, response);
        } finally {
            // DB 연결 자원 닫기
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}