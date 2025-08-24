package com.babpool.controller.admin.review;

import com.babpool.dao.ReviewDAO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//관리자 페이지 리뷰 - 동국 6/15

//@WebServlet("/AdminReviewUpdateServlet")
public class AdminReviewUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            String content = request.getParameter("content");

            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            boolean result = dao.updateReviewContent(reviewId, content);

            request.setAttribute("reviewUpdateResult", result);
            request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}