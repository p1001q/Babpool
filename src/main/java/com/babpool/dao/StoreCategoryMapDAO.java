package com.babpool.dao;

import com.babpool.dto.StoreCategoryMapDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreCategoryMapDAO {
    private Connection conn;

    public StoreCategoryMapDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. INSERT (DTO 사용)
    public boolean insertStoreCategoryMap(StoreCategoryMapDTO dto) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO store_category_map (store_id, category_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dto.getStoreId());
            pstmt.setInt(2, dto.getCategoryId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreCategoryMapDAO] insertStoreCategoryMap() INSERT-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } 
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreCategoryMapDAO] insertStoreCategoryMap() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 2. SELECT by store_id
    public List<StoreCategoryMapDTO> getCategoriesByStoreId(int storeId) {
        List<StoreCategoryMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_category_map WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreCategoryMapDTO dto = new StoreCategoryMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setCategoryId(rs.getInt("category_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreCategoryMapDAO] getCategoriesByStoreId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); } 
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreCategoryMapDAO] getCategoriesByStoreId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 3. DELETE
    public boolean deleteMapping(int storeId, int categoryId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM store_category_map WHERE store_id = ? AND category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, categoryId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreCategoryMapDAO] deleteMapping() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } 
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreCategoryMapDAO] deleteMapping() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 4. SELECT by category_id
    public List<StoreCategoryMapDTO> getStoresByCategoryId(int categoryId) {
        List<StoreCategoryMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_category_map WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreCategoryMapDTO dto = new StoreCategoryMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setCategoryId(rs.getInt("category_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreCategoryMapDAO] getStoresByCategoryId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); } 
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreCategoryMapDAO] getStoresByCategoryId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }
}
