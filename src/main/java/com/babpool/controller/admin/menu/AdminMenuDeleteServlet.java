package com.babpool.controller.admin.menu;

import com.babpool.dao.MenuDAO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminMenuDeleteServlet")
public class AdminMenuDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            conn = DBUtil.getConnection();
            MenuDAO dao = new MenuDAO(conn);
            dao.deleteMenu(menuId);
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
