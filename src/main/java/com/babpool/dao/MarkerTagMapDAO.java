package com.babpool.dao;

import com.babpool.dto.MarkerTagMapDTO;
import com.babpool.dto.TagDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarkerTagMapDAO {

    private Connection conn;

    public MarkerTagMapDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. 마커-태그 매핑 추가
    public boolean insertMarkerTag(MarkerTagMapDTO dto) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO marker_tag_map (marker_id, tag_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dto.getMarkerId());
            pstmt.setInt(2, dto.getTagId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerTagMapDAO] insertMarkerTag() INSERT-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerTagMapDAO] insertMarkerTag() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 2. 마커 ID로 태그들 조회
    public List<MarkerTagMapDTO> getTagsByMarkerId(int markerId) {
        List<MarkerTagMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM marker_tag_map WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MarkerTagMapDTO dto = new MarkerTagMapDTO();
                dto.setMarkerId(rs.getInt("marker_id"));
                dto.setTagId(rs.getInt("tag_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerTagMapDAO] getTagsByMarkerId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerTagMapDAO] getTagsByMarkerId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return list;
    }

    // 3. 태그 ID로 마커들 조회
    public List<Integer> getMarkerIdsByTagId(int tagId) {
        List<Integer> markerIds = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT marker_id FROM marker_tag_map WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                markerIds.add(rs.getInt("marker_id"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerTagMapDAO] getMarkerIdsByTagId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerTagMapDAO] getMarkerIdsByTagId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return markerIds;
    }

    // 4. 삭제
    public boolean deleteByMarkerId(int markerId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM marker_tag_map WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerTagMapDAO] deleteByMarkerId() DELETE-Error: " + ex.getMessage());
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerTagMapDAO] deleteByMarkerId() pstmt close Error: " + ex.getMessage());
            }
        }
    }

    // 5. 스토어ID로 태그 조회 (수연 기능 포함)
    public List<TagDTO> getTagsByStoreId(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<TagDTO> tagList = new ArrayList<>();

        try {
            String sql = """
                SELECT t.tag_id, t.name
                FROM marker_tag_map mtm
                JOIN marker m ON mtm.marker_id = m.marker_id
                JOIN store s ON m.store_id = s.store_id
                JOIN tag t ON mtm.tag_id = t.tag_id
                WHERE s.store_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TagDTO tag = new TagDTO();
                tag.setTagId(rs.getInt("tag_id"));
                tag.setName(rs.getString("name"));
                tagList.add(tag);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerTagMapDAO] getTagsByStoreId() READ-Error: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerTagMapDAO] getTagsByStoreId() 자원 정리 Error: " + ex.getMessage());
            }
        }
        return tagList;
    }
}
