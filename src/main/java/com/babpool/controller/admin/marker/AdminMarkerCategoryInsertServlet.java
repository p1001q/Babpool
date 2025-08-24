package com.babpool.controller.admin.marker;

import com.babpool.dao.MarkerCategoryMapDAO;
import com.babpool.dto.MarkerCategoryMapDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminMarkerCategoryInsertServlet")
public class AdminMarkerCategoryInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int markerId = Integer.parseInt(request.getParameter("markerId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));

            MarkerCategoryMapDTO dto = new MarkerCategoryMapDTO();
            dto.setMarkerId(markerId);
            dto.setCategoryId(categoryId);

            conn = DBUtil.getConnection();
            MarkerCategoryMapDAO dao = new MarkerCategoryMapDAO(conn);
            dao.insertMarkerCategory(dto);

            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
