<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- ✅ Bootstrap + Custom CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/changePassword.css">


<main class="signup-container">
    <h2>비밀번호 변경</h2>

    <form action="<%= request.getContextPath() %>/ChangePasswordServlet" method="post">
        <div class="form-group">
            <label for="newPassword">새 비밀번호 <span id="pw-warning"></span></label>
            <input type="password" id="newPassword" name="newPassword" required oninput="checkPasswordPattern()">
        </div>

        <div class="form-group">
            <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="submit-btn">비밀번호 변경</button>
    </form>
</main>