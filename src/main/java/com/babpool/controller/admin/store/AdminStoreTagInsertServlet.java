package com.babpool.controller.admin.store;

import com.babpool.dao.StoreTagMapDAO;
import com.babpool.dto.StoreTagMapDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminStoreTagInsertServlet")
public class AdminStoreTagInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            // 파라미터 받아오기
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            int tagId = Integer.parseInt(request.getParameter("tagId"));

            // DTO 생성 및 값 세팅
            StoreTagMapDTO dto = new StoreTagMapDTO();
            dto.setStoreId(storeId);
            dto.setTagId(tagId);

            // DB 연결 및 DAO 사용
            conn = DBUtil.getConnection();
            StoreTagMapDAO dao = new StoreTagMapDAO(conn);
            dao.insertStoreTagMap(dto);

            // 성공시 리다이렉트
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            try { if (conn != null) conn.close(); } catch (Exception e) {} 
        }
    }
}
