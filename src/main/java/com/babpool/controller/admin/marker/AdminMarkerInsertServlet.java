package com.babpool.controller.admin.marker;

import com.babpool.dao.MarkerDAO;
import com.babpool.dao.StoreDAO;
import com.babpool.dto.MarkerDTO;
import com.babpool.dto.StoreDTO;
import com.babpool.utils.DBUtil;
import com.babpool.utils.NaverMapUrlUtil;
import com.babpool.utils.SearchAndConvertPlaceUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;

//@WebServlet("/AdminMarkerInsertServlet")
public class AdminMarkerInsertServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");
            int storeId = Integer.parseInt(request.getParameter("storeId"));

            // storeId 기반 가게 정보 조회
            conn = DBUtil.getConnection();
            StoreDAO storeDAO = new StoreDAO(conn);
            StoreDTO store = storeDAO.getStoreById(storeId);
            if (store == null) {
                response.sendRedirect("manageStorePage.jsp?error=notfound");
                return;
            }

            String placeName = store.getName();
            double[] wgs = SearchAndConvertPlaceUtil.getPlaceCoordinates(placeName);
            if (wgs == null) {
                response.sendRedirect("manageStorePage.jsp?error=placefail");
                return;
            }

            double[] tm = SearchAndConvertPlaceUtil.convertWGS84To3857(wgs[0], wgs[1]);
            String unicode = URLEncoder.encode(placeName, "UTF-8");

            // 수동 추출한 placeId를 추후 관리자가 입력해야 함
            int placeId = 0;  // 또는 DB에서 가져오는 구조로 변경 가능
            String directionUrl = NaverMapUrlUtil.generateDirectionUrl(tm[0], tm[1], unicode, placeId);

            // DTO 구성
            MarkerDTO dto = new MarkerDTO();
            dto.setStoreId(storeId);
            dto.setStoreName(placeName);
            dto.setWgsX(wgs[0]);
            dto.setWgsY(wgs[1]);
            dto.setTmX(tm[0]);
            dto.setTmY(tm[1]);
            dto.setUnicodeName(unicode);
            dto.setPlaceId(placeId);
            dto.setUrl(directionUrl);

            // DB 저장
            MarkerDAO dao = new MarkerDAO(conn);
            boolean success = dao.insertMarker(dto);

            if (success) {
                request.setAttribute("markerInsertResult", true);
                request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
            } else {
                request.setAttribute("markerInsertResult", false);
                request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
            }
            
//            response.sendRedirect("manageMarkerPage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manageMarkerPage.jsp");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}

