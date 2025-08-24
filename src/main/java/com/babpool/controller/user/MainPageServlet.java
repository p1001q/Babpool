//package com.babpool.controller.user;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.util.List;
//import java.util.Map;
//
//import com.babpool.dao.BookmarkDAO;
//import com.babpool.dao.CategoryDAO;
//import com.babpool.dao.StoreDAO;
//import com.babpool.dao.TagDAO;
//import com.babpool.dto.CategoryDTO;
//import com.babpool.dto.StoreDTO;
//import com.babpool.dto.TagDTO;
//import com.babpool.dto.UserDTO;
//import com.babpool.utils.DBUtil;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//@WebServlet("/mainPage")
//public class MainPageServlet extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Connection conn = null;
//
//        try {
//            conn = DBUtil.getConnection();
//
//            // 세션에서 유저 정보 가져오기
//            HttpSession session = request.getSession();
//            UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
//            int userId = (loginUser != null) ? loginUser.getUserId() : -1;
//
//            // 카테고리와 태그 정보 조회
//            CategoryDAO categoryDAO = new CategoryDAO(conn);
//            TagDAO tagDAO = new TagDAO(conn);
//            List<CategoryDTO> categoryList = categoryDAO.getAllCategories();
//            List<TagDTO> tagList = tagDAO.getAllTags();
//
//            // Top 3 가게 정보 조회
//            StoreDAO storeDAO = new StoreDAO(conn);
//            List<Map<String, Object>> topStores = storeDAO.getTop3StoresWithCategory();
//
//            // 북마크 DAO
//            BookmarkDAO bookmarkDAO = new BookmarkDAO(conn);
//
//            // Top 3 가게별 북마크 여부 확인
//            for (Map<String, Object> topStore : topStores) {
//                int topStoreId = (Integer) topStore.get("storeId");
//                boolean isBookmarked = false;
//                if (userId != -1) {
//                    isBookmarked = bookmarkDAO.isBookmarked(userId, topStoreId);
//                }
//                System.out.println("storeId=" + topStoreId + ", isBookmarked=" + isBookmarked); //디버깅용
//                topStore.put("isBookmarked", Boolean.valueOf(isBookmarked)); //디버깅 부분. 타입 확실히 Boolean 표시
//            }
//            
//
//            // 파라미터 mascot (마스코트 클릭용)
//            String mascotParam = request.getParameter("mascot");
//            boolean mascotClicked = "true".equals(mascotParam);
//
//            // JSP로 데이터 전달
//            request.setAttribute("categoryList", categoryList);
//            request.setAttribute("tagList", tagList);
//            request.setAttribute("topStores", topStores);
//            request.setAttribute("mascotClicked", mascotClicked);
//            
//            System.out.println("topStores size: " + topStores.size());
//
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/mainPage.jsp");
//            dispatcher.forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(500, "서버 내부 오류 발생");
//        } finally {
//            try { if (conn != null) conn.close(); } catch (Exception e) {}
//        }
//    }
//}
