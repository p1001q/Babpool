<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*, com.babpool.dao.*, com.babpool.dto.*, com.babpool.utils.DBUtil, java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
    <title>회원 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/manageUser.css" />
</head>
<body>
    <div class="manage-container">
        <h2>회원 관리</h2>
        
        <div style="text-align: right; margin: 10px 20px; font-size: 0.85rem;">
	        <a href="<%=request.getContextPath()%>/adminMainPage.jsp" style="color: #4CAF50; text-decoration: none;">관리자 메인으로 이동</a>
	    </div>

        <!-- 전체 조회 -->
        <form action="<%=request.getContextPath()%>/AdminUserSelectAllServlet" method="get">
            <button type="submit" class="btn">전체 조회</button>
        </form>

        <!-- 선택 조회 -->
        <form action="<%=request.getContextPath()%>/AdminUserSelectByIdServlet" method="get">
            <input type="number" name="userId" placeholder="회원 ID 입력" required />
            <button type="submit" class="btn">선택 조회</button>
        </form>

        <!-- 수정 -->
        <form action="<%=request.getContextPath()%>/AdminUserUpdateServlet" method="post">
            <input type="number" name="userId" placeholder="수정할 회원 ID" required />
            <input type="text" name="nickname" placeholder="닉네임 수정" />
            <button type="submit" class="btn">수정</button>
        </form>

        <c:if test="${not empty updateResult}">
            <div class="result-message">
                <c:choose>
                    <c:when test="${updateResult}">
                        ✅ 닉네임 수정 성공
                    </c:when>
                    <c:otherwise>
                        ❌ 닉네임 수정 실패
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 삭제 -->
        <form action="<%=request.getContextPath()%>/AdminUserDeleteServlet" method="post">
            <input type="number" name="userId" placeholder="삭제할 회원 ID" required />
            <button type="submit" class="btn">삭제</button>
        </form>

        <c:if test="${not empty deleteResult}">
            <div class="result-message">
                <c:choose>
                    <c:when test="${deleteResult}">
                        ✅ 회원 삭제 성공
                    </c:when>
                    <c:otherwise>
                        ❌ 회원 삭제 실패
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 전체 조회 결과 -->
        <c:if test="${not empty userList}">
            <h3>전체 회원 목록</h3>
            <table class="simple-table">
                <tr>
                    <th>ID</th>
                    <th>닉네임</th>
                    <th>이메일</th>
                    <th>가입일</th>
                </tr>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.nickname}</td>
                        <td>${user.email}</td>
                        <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <!-- 단일 조회 결과 -->
        <c:if test="${not empty user}">
            <h3>선택한 회원 정보</h3>
            <div>
                회원 ID: ${user.userId}<br>
                닉네임: ${user.nickname}<br>
                이메일: ${user.email}<br>
                가입일: <fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm" />
            </div>
        </c:if>

    </div>
</body>
</html>