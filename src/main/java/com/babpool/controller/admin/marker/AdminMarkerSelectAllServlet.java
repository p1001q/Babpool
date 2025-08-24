package com.babpool.controller.admin.marker;

import com.babpool.dao.MarkerDAO;
import com.babpool.dto.MarkerDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

//@WebServlet("/AdminMarkerSelectAllServlet")
public class AdminMarkerSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toggle = request.getParameter("toggle");
        if (toggle == null) {
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
            return;
        }
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            MarkerDAO dao = new MarkerDAO(conn);
            List<MarkerDTO> list = dao.getAllMarkers();
            request.setAttribute("markerList", list);
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
