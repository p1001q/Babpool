package com.babpool.controller.admin.menu;

import com.babpool.dao.MenuDAO;
import com.babpool.dto.MenuDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminMenuInsertServlet")
public class AdminMenuInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int storeId = Integer.parseInt(request.getParameter("storeId"));
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));

            MenuDTO dto = new MenuDTO();
            dto.setStoreId(storeId);
            dto.setName(name);
            dto.setPrice(price);

            conn = DBUtil.getConnection();
            MenuDAO dao = new MenuDAO(conn);
            dao.insertMenu(dto);

            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
