package com.babpool.controller.admin.store;

import com.babpool.dao.StoreDAO;
import com.babpool.dto.StoreDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

//@WebServlet("/AdminStoreSelectAllServlet")
public class AdminStoreSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toggle = request.getParameter("toggle");
        if (toggle == null) {
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
            return;
        }
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            StoreDAO dao = new StoreDAO(conn);
            List<StoreDTO> list = dao.getAllStores();
            request.setAttribute("storeList", list);
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
