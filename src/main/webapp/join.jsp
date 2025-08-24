<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    String authStatus = (String) session.getAttribute("emailAuthStatus");
    if ("fail".equals(authStatus)) {
        session.removeAttribute("emailAuthStatus");
        session.removeAttribute("enteredEmail");
        session.removeAttribute("enteredCode");
%>
    <script>
        alert("인증 시간이 만료되었거나 실패했습니다. 다시 진행해주세요.");
        location.href = "join.jsp";
    </script>
<%
    return;
    }
    String email = (String) session.getAttribute("enteredEmail");
    String code = (String) session.getAttribute("enteredCode");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/join.css">
    <script src="<%=request.getContextPath()%>/resource/js/validation.js"></script>
</head>
<body>

<header class="main-header">
    <div class="logo-area">
        <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
        <div class="logo-title">BabPool</div>
    </div>
    <div class="header-icons">
        <div class="header-icon-item" onclick="location.href='login.jsp'">
            <span>🔑</span><span>로그인</span>
        </div>
        <div class="header-icon-item" onclick="location.href='mainPage.jsp'">
            <span>🏠</span><span>홈</span>
        </div>
    </div>
</header>

<main class="signup-container">
    <h2>회원가입</h2>

    <!-- 이메일 인증 form -->
    <form action="<%= request.getContextPath() %>/SendEmailServlet" method="post" target="hiddenFrame">
        <div class="form-group">
            <label for="email">이메일</label>
            <div class="email-with-button">
                <input type="email" id="email" name="email" required value="<%= email != null ? email : "" %>">
                <button type="submit" onclick="startTimer()">이메일 인증</button>
            </div>
        </div>
    </form>

    <!-- 인증번호 확인 form -->
    <form action="<%= request.getContextPath() %>/SendEmailServlet" method="post">
        <div class="form-group">
            <label for="code">인증번호</label>
            <div class="code-with-button">
                <input type="text" id="code" name="code" required value="<%= code != null ? code : "" %>">
                <button type="submit">확인</button>
                <span id="timer" class="timer-text"></span>
            </div>
        </div>
    </form>

    <!-- 회원가입 form -->
    <!-- <form action="RegisterServlet" method="post" onsubmit="return validateForm();">
        <div class="form-group">
            <label for="studentId">학번</label>
            <input type="text" id="studentId" name="studentId" required>
        </div>

        <div class="form-group">
            <label for="password">
                비밀번호
                <span id="pw-warning"></span>
            </label>
            <input type="password" id="password" name="password" required oninput="checkPasswordPattern()">
        </div>

        <div class="form-group">
            <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="submit-btn">가입하기</button>
    </form> -->

	<!-- 회원가입 form -->
	<form action="<%= request.getContextPath() %>/JoinServlet" method="post" onsubmit="return validateForm();">
	    <div class="form-group">
	        <label for="nickname">닉네임</label>
	        <input type="text" id="nickname" name="nickname" required>
	    </div>
	
	    <div class="form-group">
	        <label for="email">이메일</label>
	        <input type="email" id="email" name="email" required value="<%= email != null ? email : "" %>">
	    </div>
	
	    <div class="form-group">
	        <label for="password">
	            비밀번호
	            <span id="pw-warning"></span>
	        </label>
	        <input type="password" id="password" name="password" required oninput="checkPasswordPattern()">
	    </div>
	
	    <div class="form-group">
	        <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
	        <input type="password" id="confirmPassword" name="confirmPassword" required>
	    </div>
	
	    <button type="submit" class="submit-btn">가입하기</button>
	</form>
	
	
    <iframe name="hiddenFrame" style="display:none;"></iframe>
</main>

<footer class="page-footer">
    ⓒ 김동국. 박수연
</footer>

<script>
function checkPasswordMatch() {
    const pw = document.getElementById('password').value;
    const confirm = document.getElementById('confirmPassword').value;
    const icon = document.getElementById('pw-match-icon');

    if (confirm === "") {
        icon.innerText = '';
    } else if (pw === confirm) {
        icon.innerText = '✓';
        icon.style.color = 'green';
    } else {
        icon.innerText = '✗';
        icon.style.color = 'red';
    }
}
document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);
</script>

</body>
</html>
 