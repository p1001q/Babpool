package com.babpool.dao;

import com.babpool.dto.StoreTagMapDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreTagMapDAO {
    private Connection conn;

    public StoreTagMapDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. INSERT (DTO 사용)
    public boolean insertStoreTagMap(StoreTagMapDTO dto) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO store_tag_map (store_id, tag_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dto.getStoreId());
            pstmt.setInt(2, dto.getTagId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] insertStoreTagMap() INSERT-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] insertStoreTagMap() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 2. SELECT by store_id
    public List<StoreTagMapDTO> getTagsByStoreId(int storeId) {
        List<StoreTagMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_tag_map WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreTagMapDTO dto = new StoreTagMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setTagId(rs.getInt("tag_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] getTagsByStoreId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] getTagsByStoreId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 3. DELETE
    public boolean deleteMapping(int storeId, int tagId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM store_tag_map WHERE store_id = ? AND tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, tagId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] deleteMapping() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] deleteMapping() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 4. SELECT by tag_id
    public List<StoreTagMapDTO> getStoresByTagId(int tagId) {
        List<StoreTagMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_tag_map WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreTagMapDTO dto = new StoreTagMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setTagId(rs.getInt("tag_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] getStoresByTagId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] getStoresByTagId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }
}
