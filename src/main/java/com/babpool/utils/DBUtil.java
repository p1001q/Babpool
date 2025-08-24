package com.babpool.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/babpooldb?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
            String user = "root";         // MySQL 계정명
            String password = ApiKeyUtil.get("mysqlPassword");  // root 비번 입력

            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    //수연 todo
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}