<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />

<%
    HttpSession sessionObj = request.getSession(false);
    String nickname = null;
    if (sessionObj != null) {
        nickname = (String) sessionObj.getAttribute("nickname");
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>BabPool - <fmt:message key="login.title"/></title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/bootstrap.min.css" />
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/login.css" />
</head>

<body>
  <!-- ✅ 헤더 -->
  <header class="main-header">
    <div class="logo-area">
      <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
      <span class="logo-title">BabPool</span>
    </div>
    <div class="header-icons">
      <div class="header-icon-item" onclick="location.href='mainPage.jsp'">
        <span>🏠</span><span>홈</span>
      </div>

      <%-- 로그인 상태일 경우 닉네임과 로그아웃 표시 --%>
      <%
        if (nickname != null) {
      %>
        <div class="header-icon-item" style="margin-left: 20px;">
          <span>👤 <%= nickname %></span>
          <a href="<%= request.getContextPath() %>/LogoutServlet" style="margin-left: 10px;">로그아웃</a>
        </div>
      <%
        }
      %>
    </div>
  </header>

  <!-- ✅ 로그인 박스 -->
  <div class="login-wrapper">
    <form action="<%= request.getContextPath() %>/LoginServlet" method="post" class="login-box">
      <!-- 입력창 영역 -->
      <div class="input-fields">
        <input type="email" name="email" placeholder="<fmt:message key='login.email'/>" required />
        <input type="password" name="password" placeholder="<fmt:message key='login.password'/>" required />
        
        <!-- 로그인 실패 시 메시지 출력 -->
        <%
          String error = request.getParameter("error");
          if ("invalid".equals(error)) {
        %>
            <p style="color: red;">⚠ <fmt:message key="login.error.invalid"/></p>
        <%
          } else if ("server".equals(error)) {
        %>
            <p style="color: red;">⚠ <fmt:message key="login.error.server"/></p>
        <%
          }
        %>


        <div class="links">
          <a href="<%= request.getContextPath() %>/join.jsp"><fmt:message key="login.signup"/></a>
        </div>
      </div>
	  
	  <!-- 로그인 버튼 영역 -->
		<div class="login-btn-area">
		  <div class="login-btn-wrapper">
		    <button type="submit" class="login-btn"><fmt:message key="login.loginBtn"/></button>
		    <div class="remember-me">
		      <label>
				  <input type="checkbox" name="rememberMe" value="true">
				  <fmt:message key="login.remember"/>
			  </label>
		    </div>
		  </div>
		</div>
    </form>
  </div>

  <!-- ✅ 푸터 -->
  <footer class="page-footer">
    ⓒ 김동국. 박수연
  </footer>
</body>
</html>
 