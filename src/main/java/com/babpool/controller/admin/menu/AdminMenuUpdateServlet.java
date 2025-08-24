package com.babpool.controller.admin.menu;

import com.babpool.dao.MenuDAO;
import com.babpool.dto.MenuDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminMenuUpdateServlet")
public class AdminMenuUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int menuId = Integer.parseInt(request.getParameter("menuId"));
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");

            conn = DBUtil.getConnection();
            MenuDAO dao = new MenuDAO(conn);
            MenuDTO dto = dao.getMenuById(menuId);

            if (dto != null) {
                if (!name.isEmpty()) dto.setName(name);
                if (!priceStr.isEmpty()) dto.setPrice(Integer.parseInt(priceStr));
                dao.updateMenu(dto);
            }
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
