<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- ✅ 다국어 설정: 기본 한국어, session 기준 설정 --%>
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />

<!-- ✅ Bootstrap + Custom CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">

<!-- ✅ HEADER -->
<header class="main-header">
  <div class="logo-area">
    <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
    <span class="logo-title">BabPool</span>
    <span class="clock" id="clock">--:--</span>
  </div>



  <!-- ✅ 로그인 / 홈 -->
  <div class="header-icons">
  <!-- 로그인 or 마이페이지 버튼 -->
  <div class="header-icon-item">
     <%-- <c:choose>
      <c:when test="${not empty sessionScope.loginUser}">
        <a href="<%= request.getContextPath() %>/myPage.jsp" class="header-button">
          <span>👤</span>
          <span>마이페이지</span>
        </a>
      </c:when>
      <c:otherwise>
        <a href="<%= request.getContextPath() %>/login.jsp" class="header-button">
          <span>🔑</span>
          <span><fmt:message key="header.login" /></span>
        </a>
      </c:otherwise>
    </c:choose> --%>
    
    <c:choose>
  <c:when test="${not empty sessionScope.loginUser}">
  
    <!-- ✅ 관리자 계정 여부 확인 -->
    <c:choose>
      <c:when test="${sessionScope.loginUser.email == 'ehdrnr65@skuniv.ac.kr' || 'tndus10@skuniv.ac.kr'}">
        <a href="<%= request.getContextPath() %>/adminMainPage.jsp" class="header-button">
          <span>🛠️</span>
          <span>관리자</span>
        </a>
        <a href="<%= request.getContextPath() %>/myPage.jsp" class="header-button">
          <span>👤</span>
          <span>마이페이지</span>
        </a>
      </c:when>
      <c:otherwise>
        <a href="<%= request.getContextPath() %>/myPage.jsp" class="header-button">
         <span>👤</span>
		<span><fmt:message key="header.mypage" /></span>
        </a>
      </c:otherwise>
    </c:choose>

  </c:when>

  <c:otherwise>
    <a href="<%= request.getContextPath() %>/login.jsp" class="header-button">
      <span>🔑</span>
      <span><fmt:message key="header.login" /></span>
    </a>
  </c:otherwise>
</c:choose>
    
  </div>

  <!-- 홈 버튼은 그대로 유지 -->
  <div class="header-icon-item">
    <a href="<%= request.getContextPath() %>/mainPage.jsp" class="header-button">
      <span>🏠</span>
      <span><fmt:message key="header.home" /></span>
    </a>
  </div>
</div>

</header>

<!-- ✅ FOOTER -->
<footer class="page-footer">
  ⓒ 김동국. 박수연
</footer>

<!-- ✅ CLOCK SCRIPT -->
<script>
  function pad2(n) {
    return n < 10 ? '0' + n : '' + n;
  }

  function updateClock() {
    const now = new Date();
    const clockText = pad2(now.getMonth() + 1) + '/' + pad2(now.getDate()) +
                      '<br>' + pad2(now.getHours()) + ':' + pad2(now.getMinutes());
    const clock = document.getElementById('clock');
    if (clock) clock.innerHTML = clockText;
  }

  window.addEventListener('load', () => {
    updateClock();
    setInterval(updateClock, 1000);
  });
</script>