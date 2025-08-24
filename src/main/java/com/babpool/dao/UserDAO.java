package com.babpool.dao;

import com.babpool.dto.UserDTO;
import com.babpool.filter.LogFileFilter;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // 회원 추가 (회원가입)
    public boolean insertUser(UserDTO user) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO user (nickname, email, password, profile_image_path) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getNickname());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getProfileImagePath());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] insertUser() INSERT-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 회원 단일 조회 (PK로 조회)
    public UserDTO getUserById(int userId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserDTO user = null;
        try {
            String sql = "SELECT * FROM user WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = mapUserDTO(rs);
            }
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] getUserById() READ-Error", ex);
        } finally {
            close(rs, pstmt);
        }
        return user;
    }

    // 전체 회원 리스트 조회
    public List<UserDTO> getAllUsers() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UserDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user ORDER BY user_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapUserDTO(rs));
            }
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] getAllUsers() READ-Error", ex);
        } finally {
            close(rs, pstmt);
        }
        return list;
    }

    // 회원 정보 수정
    public boolean updateUser(UserDTO user) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE user SET nickname = ?, password = ?, profile_image_path = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getNickname());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getProfileImagePath());
            pstmt.setInt(4, user.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] updateUser() UPDATE-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 회원 삭제
    public boolean deleteUser(int userId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM user WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] deleteUser() DELETE-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 이메일 기준으로 회원 조회
    public UserDTO getUserByEmail(String email) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserDTO user = null;
        try {
            String sql = "SELECT * FROM user WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = mapUserDTO(rs);
            }
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] getUserByEmail() READ-Error", ex);
        } finally {
            close(rs, pstmt);
        }
        return user;
    }

    // 이메일 기준 삭제 (mypage 탈퇴)
    public boolean deleteUserByEmail(String email) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM user WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] deleteUserByEmail() DELETE-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 비밀번호 변경 (이메일 기준)
    public boolean updatePasswordByEmail(String email, String newPassword) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE user SET password = ? WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] updatePasswordByEmail() UPDATE-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 프로필 이미지 업데이트
    public boolean updateUserProfileImage(UserDTO user) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE user SET profile_image_path = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getProfileImagePath());
            pstmt.setInt(2, user.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException("[UserDAO] updateUserProfileImage() UPDATE-Error", ex);
            return false;
        } finally {
            close(pstmt);
        }
    }

    // 공통 ResultSet → DTO 매핑
    private UserDTO mapUserDTO(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setUserId(rs.getInt("user_id"));
        user.setNickname(rs.getString("nickname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setProfileImagePath(rs.getString("profile_image_path"));
        return user;
    }

    // 공통 예외 처리
    private void handleSQLException(String message, SQLException ex) {
        PrintWriter writer = LogFileFilter.getWriter();
        if (writer != null) {
            writer.println(message);
            ex.printStackTrace(writer);
        } else {
            ex.printStackTrace();
        }
    }

    // 공통 자원 해제
    private void close(ResultSet rs, PreparedStatement pstmt) {
        try { if (rs != null) rs.close(); } catch (SQLException ex) { logCloseError(ex); }
        try { if (pstmt != null) pstmt.close(); } catch (SQLException ex) { logCloseError(ex); }
    }
    private void close(PreparedStatement pstmt) {
        try { if (pstmt != null) pstmt.close(); } catch (SQLException ex) { logCloseError(ex); }
    }

    // 자원 정리 예외 처리
    private void logCloseError(SQLException ex) {
        PrintWriter writer = LogFileFilter.getWriter();
        if (writer != null) {
            writer.println("[UserDAO] 자원 정리 오류");
            ex.printStackTrace(writer);
        } else {
            ex.printStackTrace();
        }
    }
}
