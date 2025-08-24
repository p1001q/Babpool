package com.babpool.controller.admin.review;

import com.babpool.dao.ReviewDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

//관리자 페이지 리뷰 - 동국 6/15

//@WebServlet("/AdminReviewSelectAllServlet")
public class AdminReviewSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            List<ReviewDTO> reviewList = dao.getAllReviews();

            request.setAttribute("reviewList", reviewList);
            request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}