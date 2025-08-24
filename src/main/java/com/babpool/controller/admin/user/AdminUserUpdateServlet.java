package com.babpool.controller.admin.user;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminUserUpdateServlet")
public class AdminUserUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String nickname = request.getParameter("nickname");

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            UserDTO user = userDAO.getUserById(userId);

            boolean result = false;
            if (user != null) {
                user.setNickname(nickname);
                result = userDAO.updateUser(user);
            }
            request.setAttribute("updateResult", result);
            request.getRequestDispatcher("manageUserPage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
