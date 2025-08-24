package com.babpool.controller.admin.user;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

//@WebServlet("/AdminUserSelectAllServlet")
public class AdminUserSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            List<UserDTO> userList = userDAO.getAllUsers();
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("manageUserPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
