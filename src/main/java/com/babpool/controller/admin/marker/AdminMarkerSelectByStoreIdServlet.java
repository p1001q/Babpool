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

//@WebServlet("/AdminMarkerSelectByStoreIdServlet")
public class AdminMarkerSelectByStoreIdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            conn = DBUtil.getConnection();
            MarkerDAO dao = new MarkerDAO(conn);

            // 단일 마커 조회 (store_id 기준)
            List<MarkerDTO> list = dao.getAllMarkers();
            MarkerDTO result = null;
            for (MarkerDTO m : list) {
                if (m.getStoreId() == storeId) {
                    result = m;
                    break;
                }
            }
            request.setAttribute("marker", result);
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
