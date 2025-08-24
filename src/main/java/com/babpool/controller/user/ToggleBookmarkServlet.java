package com.babpool.controller.user;

import com.babpool.dao.BookmarkDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/ToggleBookmarkServlet")
public class ToggleBookmarkServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();

            int storeId = Integer.parseInt(request.getParameter("storeId"));

            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = loginUser.getUserId();

            BookmarkDAO bookmarkDAO = new BookmarkDAO(conn);

            boolean isBookmarked = bookmarkDAO.isBookmarked(userId, storeId);

            if (isBookmarked) {
                bookmarkDAO.removeBookmark(userId, storeId);
            } else {
                bookmarkDAO.addBookmark(userId, storeId);
            }

           
            // 찜 토글 후 다시 placeDetail로 이동
            response.sendRedirect(request.getContextPath() + "/PlaceDetailServlet?storeId=" + storeId);


        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "찜 토글 처리 중 서버 오류 발생");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}