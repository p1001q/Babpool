package com.babpool.controller.admin.store;

import com.babpool.dao.StoreDAO;
import com.babpool.dto.StoreDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminStoreInsertServlet")
public class AdminStoreInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");
            StoreDTO dto = new StoreDTO();
            dto.setName(request.getParameter("name"));
            dto.setAddress(request.getParameter("address"));
            dto.setPhone(request.getParameter("phone"));
            dto.setOpenTime(request.getParameter("openTime"));
            dto.setRatingAvg(0);
            dto.setReviewCount(0);
            dto.setLikeCount(0);

            conn = DBUtil.getConnection();
            StoreDAO dao = new StoreDAO(conn);
            dao.insertStore(dto);
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
