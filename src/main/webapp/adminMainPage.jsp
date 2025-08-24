<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.babpool.dto.*, com.babpool.utils.DBUtil" %>

<%
    // 세션에서 로그인 사용자 가져오기
    UserDTO admin = (UserDTO) session.getAttribute("loginUser");

    if (admin == null) {
%>
        <script>
            alert("로그인이 필요합니다.");
            location.href = "login.jsp";
        </script>
<%
        return;
    }

    // 관리자 이메일 검사
    String email = admin.getEmail();
    if (!email.equals("ehdrnr65@skuniv.ac.kr") && !email.equals("tndus10@skuniv.ac.kr")) {
%>
        <script>
            alert("관리자만 입장 가능한 페이지입니다.");
            location.href = "mainPage.jsp";
        </script>
<%
        return;
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/admin.css" />
</head>
<body>
    <div class="admin-container">
    
        <div class="admin-logo">
            <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
            <span class="logo-title">BabPool</span>
        </div>

        <div class="admin-menu">
            <button onclick="location.href='manageStorePage.jsp'">가게 관리</button>
            <button onclick="location.href='manageReviewPage.jsp'">리뷰 관리</button>
            <button onclick="location.href='manageUserPage.jsp'">회원 관리</button>
        </div>
        
    </div>
</body>
</html>