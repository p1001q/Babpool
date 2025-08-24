package com.babpool.controller.admin.store;

import com.babpool.dao.StoreDAO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminStoreDeleteServlet")
public class AdminStoreDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            conn = DBUtil.getConnection();
            StoreDAO dao = new StoreDAO(conn);
            dao.deleteStore(storeId);
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
