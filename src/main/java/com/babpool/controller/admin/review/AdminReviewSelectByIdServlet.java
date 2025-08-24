package com.babpool.controller.admin.review;

import com.babpool.dao.ReviewDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//관리자 페이지 리뷰 - 동국 6/15

//@WebServlet("/AdminReviewSelectByIdServlet")
public class AdminReviewSelectByIdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            ReviewDTO review = dao.getReviewById(reviewId);

            request.setAttribute("reviewOne", review);
            request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}