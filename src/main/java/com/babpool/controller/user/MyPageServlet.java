//// 동국 -  연결 servlet ->> myPage.jsp

//package com.babpool.controller;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.babpool.dao.*;
//import com.babpool.dto.*;
//import com.babpool.utils.DBUtil;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//
//
//@WebServlet("/MyPageServlet")
//public class MyPageServlet extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 세션에서 로그인된 사용자 정보 가져오기
//        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");
//
//        if (loginUser == null) {
//            // 로그인 안되어 있으면 로그인 페이지로 리다이렉트
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        // DB 연결
//        Connection conn = null;
//        UserDAO userDAO = null;
//        ReviewDAO reviewDAO = null;
//        BookmarkDAO bookmarkDAO = null;
//        StoreDAO storeDAO = null;
//
//        UserDTO user = null;
//        List<ReviewDTO> reviews = null;
//        List<BookmarkDTO> bookmarks = null;
//        List<StoreDTO> stores = new ArrayList<>();
//
//        try {
//            conn = DBUtil.getConnection();
//            userDAO = new UserDAO(conn);
//            reviewDAO = new ReviewDAO(conn);
//            bookmarkDAO = new BookmarkDAO(conn);
//            storeDAO = new StoreDAO(conn);
//
//            // DB에서 최신 유저 정보 가져오기 (프로필 이미지 최신화용)
//            user = userDAO.getUserByEmail(loginUser.getEmail());
//
//            // 찜한 가게, 내가 쓴 리뷰 조회
//            reviews = reviewDAO.getReviewsByUserId(user.getUserId());
//            bookmarks = bookmarkDAO.getBookmarkedStoresByUserId(user.getUserId());
//
//            for (BookmarkDTO bookmark : bookmarks) {
//                StoreDTO store = storeDAO.getStoreById(bookmark.getStoreId());
//                stores.add(store);
//            }
//
//            // JSP로 데이터 전달
//            request.setAttribute("user", user);  // 사용자 정보
//            request.setAttribute("reviews", reviews);  // 사용자 리뷰
//            request.setAttribute("stores", stores);  // 찜한 가게 목록
//
//            // 마이페이지 JSP로 포워딩
//            RequestDispatcher dispatcher = request.getRequestDispatcher("myPage.jsp");
//            dispatcher.forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();  // 예외를 출력하고
//            request.setAttribute("error", "서버 오류: " + e.getMessage());  // 오류 메시지 전달
//            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");  // 오류 페이지로 포워딩
//            dispatcher.forward(request, response);  // 에러 페이지로 이동
//        } finally {
//            try {
//                if (conn != null) conn.close();  // 연결 종료
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
