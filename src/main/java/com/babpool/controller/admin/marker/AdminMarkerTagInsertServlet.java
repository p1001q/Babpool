package com.babpool.controller.admin.marker;

import com.babpool.dao.MarkerTagMapDAO;
import com.babpool.dto.MarkerTagMapDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminMarkerTagInsertServlet")
public class AdminMarkerTagInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int markerId = Integer.parseInt(request.getParameter("markerId"));
            int tagId = Integer.parseInt(request.getParameter("tagId"));

            MarkerTagMapDTO dto = new MarkerTagMapDTO();
            dto.setMarkerId(markerId);
            dto.setTagId(tagId);

            conn = DBUtil.getConnection();
            MarkerTagMapDAO dao = new MarkerTagMapDAO(conn);
            dao.insertMarkerTag(dto);

            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
