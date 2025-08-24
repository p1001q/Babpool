package com.babpool.dao;

import com.babpool.dto.ReviewDTO;
import com.babpool.dto.ReviewImageDTO;
import com.babpool.filter.LogFileFilter;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewDAO {

    private Connection conn;

    public ReviewDAO(Connection conn) {
        this.conn = conn;
    }

    // 리뷰 등록
    public boolean insertReview(ReviewDTO review) {
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            String sql = "INSERT INTO review (user_id, store_id, rating, content) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getStoreId());
            pstmt.setDouble(3, review.getRating());
            pstmt.setString(4, review.getContent());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return false;

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setReviewId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] insertReview() INSERT-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] insertReview() 자원 정리 Error: " + ex.getMessage());
            }
        }
    }

    // 리뷰 단건 조회
    public ReviewDTO getReviewById(int reviewId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ReviewDTO review = null;
        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path
                FROM review r
                JOIN user u ON r.user_id = u.user_id
                WHERE r.review_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();

            if (rs.next()) review = mapReviewDTO(rs);
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getReviewById() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getReviewById() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return review;
    }

    // 리뷰 전체 조회 (정렬 + 이미지 연동 포함)
    public List<ReviewDTO> getReviewsByStoreSorted(int storeId, String sortType) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();
        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path 
                FROM review r 
                JOIN user u ON r.user_id = u.user_id 
                WHERE r.store_id = ?
            """;
            if ("recent".equals(sortType)) sql += " ORDER BY r.created_at DESC";
            else if ("high".equals(sortType)) sql += " ORDER BY r.rating DESC";
            else if ("low".equals(sortType)) sql += " ORDER BY r.rating ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            ReviewImageDAO imageDAO = new ReviewImageDAO(conn);
            while (rs.next()) {
                ReviewDTO review = mapReviewDTO(rs);
                List<ReviewImageDTO> images = imageDAO.getImagesByReviewId(review.getReviewId());
                review.setImages(images);
                list.add(review);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByStoreSorted() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByStoreSorted() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 특정 사용자의 리뷰 전체 조회 (마이페이지)
    public List<ReviewDTO> getReviewsByUserId(int userId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();
        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path 
                FROM review r 
                JOIN user u ON r.user_id = u.user_id 
                WHERE r.user_id = ?
                ORDER BY r.created_at DESC
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) list.add(mapReviewDTO(rs));
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByUserId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByUserId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 리뷰 수정
    public boolean updateReview(ReviewDTO review) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE review SET rating = ?, content = ? WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, review.getRating());
            pstmt.setString(2, review.getContent());
            pstmt.setInt(3, review.getReviewId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] updateReview() UPDATE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] updateReview() 자원 정리 Error: " + ex.getMessage());
            }
        }
    }

    // 리뷰 삭제
    public boolean deleteReview(int reviewId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] deleteReview() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] deleteReview() 자원 정리 Error: " + ex.getMessage());
            }
        }
    }

    // 별점 통계 (리뷰 점수 그래프)
    public Map<Integer, Integer> getScoreSummaryByStore(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, Integer> result = new HashMap<>();
        try {
            for (int i = 1; i <= 5; i++) result.put(i, 0);
            String sql = "SELECT FLOOR(rating) AS rating, COUNT(*) AS count FROM review WHERE store_id = ? GROUP BY FLOOR(rating)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("rating"), rs.getInt("count"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getScoreSummaryByStore() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getScoreSummaryByStore() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return result;
    }

    // 리뷰 → DTO 매핑 공통 함수
    private ReviewDTO mapReviewDTO(ResultSet rs) throws SQLException {
        ReviewDTO review = new ReviewDTO();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setStoreId(rs.getInt("store_id"));
        review.setRating(rs.getDouble("rating"));
        review.setContent(rs.getString("content"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        review.setNickname(rs.getString("nickname"));
        review.setProfileImagePath(rs.getString("profile_image_path"));
        review.setImages(new ArrayList<>());
        return review;
    }

    // 관리자: 리뷰 전체 조회
    public List<ReviewDTO> getAllReviews() {
        List<ReviewDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT r.*, u.nickname, u.profile_image_path FROM review r JOIN user u ON r.user_id = u.user_id ORDER BY r.review_id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) list.add(mapReviewDTO(rs));
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getAllReviews() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getAllReviews() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 관리자: 리뷰 내용 수정
    public boolean updateReviewContent(int reviewId, String content) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE review SET content = ? WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, content);
            pstmt.setInt(2, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] updateReviewContent() UPDATE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] updateReviewContent() 자원 정리 Error: " + ex.getMessage());
            }
        }
    }
}
