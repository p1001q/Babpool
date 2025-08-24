package com.babpool.controller.admin.store;

import com.babpool.dao.StoreDAO;
import com.babpool.dto.StoreDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminStoreSelectByIdServlet")
public class AdminStoreSelectByIdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            conn = DBUtil.getConnection();
            StoreDAO dao = new StoreDAO(conn);
            StoreDTO store = dao.getStoreById(storeId);
            request.setAttribute("store", store);
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
