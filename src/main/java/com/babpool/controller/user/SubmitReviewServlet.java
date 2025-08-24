package com.babpool.controller.user;

import com.babpool.dao.ReviewDAO;
import com.babpool.dao.ReviewImageDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.dto.ReviewImageDTO;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//@WebServlet("/SubmitReviewServlet")
public class SubmitReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        try {
            // ✅ 업로드 경로 생성
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            int maxSize = 50 * 1024 * 1024;  // 50MB

            // ✅ MultipartRequest 생성 (파일+폼 한번에 받음)
            MultipartRequest multi = null;
            try {
                multi = new MultipartRequest(
                        request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
            } catch (IOException e) {
                // 파일 용량 초과 등 업로드 실패 시
                response.sendRedirect("errorPage.jsp?errorType=fileSizeExceeded");
                return;
            }

            // ✅ 폼 데이터 파싱
            String userIdStr = multi.getParameter("userId");
            String userNickname = multi.getParameter("userNickname");
            String storeIdStr = multi.getParameter("storeId");
            String ratingStr = multi.getParameter("rating");
            String reviewText = multi.getParameter("reviewText");

            // ✅ 파라미터 널검증
            if (userIdStr == null || storeIdStr == null || ratingStr == null || userNickname == null) {
                response.sendRedirect("errorPage.jsp?errorType=invalidUserInfo");
                return;
            }
            if (reviewText == null || reviewText.trim().isEmpty()) {
                response.sendRedirect("errorPage.jsp?errorType=missingReviewText");
                return;
            }

            // ✅ 파라미터 파싱 (형식 검증)
            int userId = 0;
            int storeId = 0;
            double rating = 0;
            try {
                userId = Integer.parseInt(userIdStr);
                storeId = Integer.parseInt(storeIdStr);
                rating = Double.parseDouble(ratingStr);
            } catch (NumberFormatException ex) {
                response.sendRedirect("errorPage.jsp?errorType=invalidParameterFormat");
                return;
            }

            conn = DBUtil.getConnection();

            // ✅ 리뷰 본문 저장
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            ReviewDTO review = new ReviewDTO();
            review.setUserId(userId);
            review.setNickname(userNickname);
            review.setStoreId(storeId);
            review.setRating(rating);
            review.setContent(reviewText);

            boolean insertResult = reviewDAO.insertReview(review);
            if (!insertResult) {
                response.sendRedirect("errorPage.jsp?errorType=dbError");
                return;
            }

            // ✅ 단일 이미지 파일 저장 (한장 전용)
            File file = multi.getFile("image");
            if (file != null) {
                String fileName = multi.getFilesystemName("image");

                ReviewImageDTO image = new ReviewImageDTO();
                image.setReviewId(review.getReviewId());
                image.setImagePath("uploads/" + fileName);
                image.setImageOrder(1); // 항상 1번으로 저장

                ReviewImageDAO imageDAO = new ReviewImageDAO(conn);
                imageDAO.insertReviewImage(image);
            }

            PrintWriter logWriter = LogFileFilter.getWriter();
            if (logWriter != null) {
                logWriter.println("[SUBMIT REVIEW SUCCESS] : " + review.getReviewId());
                logWriter.flush();
            }
            
            
            // ✅ 등록 완료 후 상세페이지 이동
            response.sendRedirect(request.getContextPath() + "/PlaceDetailServlet?storeId=" + storeId);

        } catch (Exception e) {
            e.printStackTrace();
            
            PrintWriter logWriter = LogFileFilter.getWriter();
            if (logWriter != null) {
                logWriter.println("[SUBMIT REVIEW FAIL]");
                logWriter.flush();
            }
            
            response.sendRedirect("errorPage.jsp?errorType=dbError");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
}