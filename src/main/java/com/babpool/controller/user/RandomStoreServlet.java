package com.babpool.controller.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.babpool.dao.*;
import com.babpool.dto.*;
import com.babpool.utils.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@WebServlet("/RandomStoreServlet")
public class RandomStoreServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            StoreDAO storeDAO = new StoreDAO(conn);
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            ReviewImageDAO reviewImageDAO = new ReviewImageDAO(conn);
            TagDAO tagDAO = new TagDAO(conn);
            CategoryDAO categoryDAO = new CategoryDAO(conn);

            StoreDTO store = storeDAO.getRandomStore(); //실제 실행 코드
            
            //StoreDTO store = storeDAO.getStoreById(2); // ⭐ 임시 테스트용으로 storeId=2 고정

            int storeId = store.getStoreId();

            double averageRating = store.getRatingAvg();
            int reviewCount = store.getReviewCount();
            int likeCount = store.getLikeCount();

            List<ReviewDTO> reviewList = reviewDAO.getReviewsByStoreSorted(storeId, "recent");
            List<String> reviews = new ArrayList<>();
            for (int i = 0; i < Math.min(reviewList.size(), 3); i++) {
                reviews.add(reviewList.get(i).getContent());
            }

            List<TagDTO> tagDTOs = tagDAO.getTagsByStoreId(storeId);
            List<String> tags = tagDTOs.stream().map(TagDTO::getName).collect(Collectors.toList());

            List<CategoryDTO> categoryList = categoryDAO.getCategoriesByStoreId(storeId);
            List<String> categoryNames = categoryList.stream().map(CategoryDTO::getName).collect(Collectors.toList());

            String imagePath = reviewImageDAO.getFirstImageByStoreId(storeId);
            if (imagePath == null || imagePath.isEmpty()) {
                imagePath = request.getContextPath() + "/resource/images/default_store.png";
            }

            // ✅ JSON 수동조립 안정화버전 시작
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"storeId\":").append(storeId).append(",");
            json.append("\"storeName\":\"").append(escape(store.getName())).append("\",");
            json.append("\"ratingAvg\":").append(averageRating).append(",");
            json.append("\"reviewCount\":").append(reviewCount).append(",");
            json.append("\"likeCount\":").append(likeCount).append(",");
            json.append("\"imagePath\":\"").append(escape(imagePath)).append("\",");

            // 리뷰
            json.append("\"reviews\":[");
            if (reviews.size() > 0) {
                for (int i = 0; i < reviews.size(); i++) {
                    json.append("\"").append(escape(reviews.get(i))).append("\"");
                    if (i < reviews.size() - 1) json.append(",");
                }
            }
            json.append("],");

            // 태그
            json.append("\"tags\":[");
            if (tags.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    json.append("\"").append(escape(tags.get(i))).append("\"");
                    if (i < tags.size() - 1) json.append(",");
                }
            }
            json.append("],");


            // 카테고리
            json.append("\"categories\":[");
            if (categoryNames.size() > 0) {
                for (int i = 0; i < categoryNames.size(); i++) {
                    json.append("\"").append(escape(categoryNames.get(i))).append("\"");
                    if (i < categoryNames.size() - 1) json.append(",");
                }
            }
            json.append("]");

            json.append("}");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ex) { }
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "")
                .replace("\r", "");
    }
}
