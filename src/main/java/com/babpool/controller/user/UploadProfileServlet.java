package com.babpool.controller.user;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;

//@WebServlet("/UploadProfileServlet")
@MultipartConfig  // 추가: multipart 파일 업로드 설정
public class UploadProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 로그인 사용자 정보 가져오기
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 파일 업로드 처리
        Part filePart = request.getPart("profileImage"); // 업로드된 파일의 파트
        String fileName = filePart.getSubmittedFileName();
        
        if (fileName != null && !fileName.isEmpty()) {
            // 저장할 파일 경로 설정
            String uploadPath = getServletContext().getRealPath("/") + "resource/images/profile/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // 디렉토리가 없으면 생성
            }

            // 파일 경로 설정
            String filePath = uploadPath + fileName;

            // 파일 저장 처리
            try (InputStream fileContent = filePart.getInputStream();
                 OutputStream outStream = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileContent.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }

            // DB에 프로필 이미지 경로 업데이트
            String newProfileImagePath = "/resource/images/profile/" + fileName;  // 클라이언트가 접근할 경로
            loginUser.setProfileImagePath(newProfileImagePath);  // UserDTO에 경로 설정

            // DB 업데이트
            try (Connection conn = DBUtil.getConnection()) {
                UserDAO userDAO = new UserDAO(conn);
                boolean isUpdated = userDAO.updateUserProfileImage(loginUser);
                if (isUpdated) {
                    // 성공적으로 업데이트 후 MyPage로 리다이렉트
                    response.sendRedirect("myPage.jsp");
                } else {
                    response.sendRedirect("myPage.jsp?error=update_failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("myPage.jsp?error=db_error");
            }
        } else {
            response.sendRedirect("myPage.jsp?error=no_image");
        }
    }
}