package com.babpool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.babpool.dto.RecommendDTO;

public class RecommendDAO {
    private Connection conn;

    public RecommendDAO(Connection conn) {
        this.conn = conn;
    }

    // 전체 추천 리스트 불러오기
    public List<RecommendDTO> getAllRecommendMenus() {
        List<RecommendDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT type, name FROM recommend";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RecommendDTO dto = new RecommendDTO();
                dto.setType(rs.getInt("type"));
                dto.setName(rs.getString("name"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }

        return list;
    }
}
