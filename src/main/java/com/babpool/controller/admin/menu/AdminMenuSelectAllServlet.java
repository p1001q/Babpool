package com.babpool.controller.admin.menu;

import com.babpool.dao.MenuDAO;
import com.babpool.dto.MenuDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

//@WebServlet("/AdminMenuSelectAllServlet")
public class AdminMenuSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String toggle = request.getParameter("toggle");
        if (toggle == null) {
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
            return;
        }
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            MenuDAO menuDAO = new MenuDAO(conn);
            List<MenuDTO> menuList = menuDAO.getAllMenus();

            request.setAttribute("menuList", menuList);
            request.getRequestDispatcher("/manageStorePage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn);
        }
    }
}