<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String langParam = request.getParameter("lang");
    if (langParam != null && langParam.matches("ko|en|ch")) {
        session.setAttribute("lang", langParam);
    }
%>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><fmt:message key="logo.title"/></title>

  <!-- 외부 라이브러리와 프로젝트 내부 CSS 연결 -->
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/bootstrap.min.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/welcome.css">
</head>
<body>

  <!-- 언어 변경 버튼 -->
  <div class="language-btns">
    <button class="btn btn-outline-dark" onclick="location.href='welcome.jsp?lang=ko'">Kr</button>
    <button class="btn btn-outline-dark" onclick="location.href='welcome.jsp?lang=en'">En</button>
    <button class="btn btn-outline-dark" onclick="location.href='welcome.jsp?lang=ch'">Ch</button>
  </div>

  <!-- 메인 컨테이너 -->
  <div class="main-container">
    <div class="logo-title"><fmt:message key="logo.title"/></div>

    <div class="mascot-container">
      <div class="speech-bubble left">
        <fmt:message key="speech.left"/>
      </div>

      <!-- 마스코트 클릭 시 메인으로 이동 / 마스코트 UI -->
    <a href="<%= request.getContextPath() %>/mainPage.jsp?mascot=true" class="bob-mascot">
  		<img src="<%= request.getContextPath() %>/resource/images/mascot_open.png" alt="밥풀 마스코트">
  		<div class="click-text"><fmt:message key="click.text"/></div>
  	</a>

      <div class="speech-bubble right">
        <fmt:message key="speech.right"/>
      </div>
    </div>
  </div>

</body>
</html>