package com.babpool.controller.user;

import com.babpool.dao.ReviewDAO;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//@WebServlet("/ReviewDeleteServlet")
public class ReviewDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 리뷰 삭제할 ID 받기
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();  // DB 연결
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            
            // 리뷰 삭제
            boolean isDeleted = reviewDAO.deleteReview(reviewId);

            if (isDeleted) {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[SUBMIT REVIEW SUCCESS] : " + reviewId);
                    logWriter.flush();
                }
            	
                // 삭제 성공 시 리뷰 리스트로 리다이렉트
                response.sendRedirect("myPage.jsp");  // 예시로 마이페이지로 리다이렉트
            } else {
            	
            	PrintWriter logWriter = LogFileFilter.getWriter();
                if (logWriter != null) {
                    logWriter.println("[SUBMIT REVIEW FAIL] : " + reviewId);
                    logWriter.flush();
                }
            	
                // 실패 시 에러 메시지와 함께 다시 페이지로 돌아가도록 설정
                request.setAttribute("deleteError", "리뷰 삭제에 실패했습니다.");
                request.getRequestDispatcher("myPage.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}