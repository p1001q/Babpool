package com.babpool.dao;

import com.babpool.dto.ReviewImageDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewImageDAO {

    private Connection conn;

    public ReviewImageDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. Create
    public boolean insertReviewImage(ReviewImageDTO image) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO review_image (review_id, image_path, image_order) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, image.getReviewId());
            pstmt.setString(2, image.getImagePath());
            pstmt.setInt(3, image.getImageOrder());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] insertReviewImage() INSERT-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] insertReviewImage() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 2. Read - 리뷰 ID로 이미지 조회
    public List<ReviewImageDTO> getImagesByReviewId(int reviewId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewImageDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM review_image WHERE review_id = ? ORDER BY image_order ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ReviewImageDTO img = new ReviewImageDTO();
                img.setImageId(rs.getInt("image_id"));
                img.setReviewId(rs.getInt("review_id"));
                img.setImagePath(rs.getString("image_path"));
                img.setImageOrder(rs.getInt("image_order"));
                list.add(img);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] getImagesByReviewId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] getImagesByReviewId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 3. Delete - 개별 이미지 삭제
    public boolean deleteImage(int imageId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review_image WHERE image_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, imageId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImage() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImage() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 4. Delete - 리뷰 ID 기준 전체 이미지 삭제
    public boolean deleteImagesByReviewId(int reviewId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review_image WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImagesByReviewId() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImagesByReviewId() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 5. Read - 가게 ID로 전체 이미지 조회
    public List<String> getReviewImagesByStoreId(int storeId) {
        List<String> imagePaths = new ArrayList<>();
        String sql = """
            SELECT ri.image_path
            FROM review_image ri
            JOIN review r ON ri.review_id = r.review_id
            WHERE r.store_id = ?
            ORDER BY ri.image_id DESC
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, storeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                imagePaths.add(rs.getString("image_path"));
            }
            rs.close();
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] getReviewImagesByStoreId() READ-Error: " + ex.getMessage());
        }
        return imagePaths;
    }
    
    // ReviewImageDAO.java 안에 추가
    public String getFirstImageByStoreId(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = """
                SELECT ri.image_path 
                FROM review_image ri 
                JOIN review r ON ri.review_id = r.review_id 
                WHERE r.store_id = ? 
                ORDER BY ri.image_id DESC 
                LIMIT 1
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("image_path");
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] getFirstImageByStoreId()-Error: " + ex.getMessage());
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException e) { LogFileFilter.getWriter().println("[ReviewImageDAO] close()-Error: " + e.getMessage()); }
        }
        return null;
    }

}
