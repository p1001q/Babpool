package com.babpool.controller.user;

import com.babpool.dao.*;
import com.babpool.dto.*;
import com.babpool.utils.DBUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

//@WebServlet("/PlaceDetailServlet")
public class PlaceDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;

        try {
        	 // ✅ 1️ storeId 파라미터 방어코드 (가장 먼저)
            String storeIdStr = request.getParameter("storeId");
            if (storeIdStr == null || storeIdStr.trim().length() == 0) {
                response.sendError(400, "storeId 파라미터가 없습니다");
                return;
            }
            int storeId = Integer.parseInt(storeIdStr);   // 여기서 한 번만 파싱

        	
            conn = DBUtil.getConnection();

            //int storeId = Integer.parseInt(request.getParameter("storeId"));

            // ⭐ 세션 로그인 유저
            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
            int userId = (loginUser != null) ? loginUser.getUserId() : -1;  // 비로그인 시 -1

            // ⭐ 가게 기본정보 (캐시 컬럼 포함)
            StoreDAO storeDAO = new StoreDAO(conn);
            StoreDTO store = storeDAO.getStoreById(storeId);
            request.setAttribute("store", store);

            // ⭐ 메뉴
            MenuDAO menuDAO = new MenuDAO(conn);
            List<MenuDTO> menus = menuDAO.getMenusByStoreId(storeId);
            request.setAttribute("menus", menus);

            // ⭐ 태그
            TagDAO tagDAO = new TagDAO(conn);
            List<TagDTO> tags = tagDAO.getTagsByStoreId(storeId);
            request.setAttribute("tags", tags);

            // ⭐ 리뷰 집계 (그래프용)
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            Map<Integer, Integer> scoreMap = reviewDAO.getScoreSummaryByStore(storeId);
            request.setAttribute("scoreMap", scoreMap);

            // ⭐ 리뷰 리스트 (정렬 지원)
            String sortType = request.getParameter("sortType");
            if (sortType == null || sortType.isEmpty()) sortType = "recent";
            List<ReviewDTO> reviews = reviewDAO.getReviewsByStoreSorted(storeId, sortType);
            request.setAttribute("reviews", reviews);
            request.setAttribute("sortType", sortType);

            // ⭐ 총 리뷰 수, 평균 평점 (캐시 사용)
            int totalCount = store.getReviewCount();
            double ratingAvg = store.getRatingAvg();
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("ratingAvg", ratingAvg);

            // ⭐ 즐겨찾기 상태 (찜 여부 + 찜 수 캐시 사용)
            BookmarkDAO bookmarkDAO = new BookmarkDAO(conn);
            boolean isBookmarked = (loginUser != null) ? bookmarkDAO.isBookmarked(userId, storeId) : false;
            int likeCount = store.getLikeCount();
            request.setAttribute("isBookmarked", isBookmarked);
            request.setAttribute("likeCount", likeCount);

            // ⭐ 카테고리
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            List<CategoryDTO> categories = categoryDAO.getCategoriesByStoreId(storeId);
            request.setAttribute("categories", categories);

            // ⭐ 마커 (길찾기 버튼용)
            MarkerDAO markerDAO = new MarkerDAO(conn);
            MarkerDTO marker = markerDAO.getMarkerById(storeId);
            request.setAttribute("marker", marker);
            
            // 리뷰 이미지 갖고 오기
            ReviewImageDAO reviewImageDAO = new ReviewImageDAO(conn);
            List<String> reviewImages = reviewImageDAO.getReviewImagesByStoreId(storeId);
            request.setAttribute("reviewImages", reviewImages);

            // JSP 이동
            request.getRequestDispatcher("/placeDetail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "서버 내부 오류 발생");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}