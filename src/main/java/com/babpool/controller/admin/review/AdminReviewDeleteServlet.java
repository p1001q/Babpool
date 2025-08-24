package com.babpool.controller.admin.review;

import com.babpool.dao.ReviewDAO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminReviewDeleteServlet")
public class AdminReviewDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            boolean result = dao.deleteReview(reviewId);

            request.setAttribute("reviewDeleteResult", result);
            request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
