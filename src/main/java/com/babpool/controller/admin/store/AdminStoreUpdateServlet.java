package com.babpool.controller.admin.store;

import com.babpool.dao.StoreDAO;
import com.babpool.dto.StoreDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/AdminStoreUpdateServlet")
public class AdminStoreUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String openTime = request.getParameter("openTime");

            conn = DBUtil.getConnection();
            StoreDAO dao = new StoreDAO(conn);
            StoreDTO store = dao.getStoreById(storeId);
            if (store != null) {
                if (!name.isEmpty()) store.setName(name);
                if (!address.isEmpty()) store.setAddress(address);
                if (!phone.isEmpty()) store.setPhone(phone);
                if (!openTime.isEmpty()) store.setOpenTime(openTime);
                dao.updateStore(store);
            }
            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
