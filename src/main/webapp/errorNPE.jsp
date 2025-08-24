<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>오류 페이지</title>

  <!-- 외부 라이브러리와 프로젝트 내부 CSS 연결 -->
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/bootstrap.min.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/welcome.css">
</head>
<body>

  <!-- 언어 변경 버튼 -->
  <div class="language-btns">
    <button class="btn btn-outline-dark" onclick="location.href='<%= request.getContextPath() %>/mainPage.jsp'">메인으로</button>
  </div>

  <!-- 메인 컨테이너 -->
  <div class="main-container">
    <div class="logo-title">입력될 부분이 입력되지 않았어요</div>

    <div class="mascot-container">
      <div class="speech-bubble left">
        문제가 발생했어요.
      </div>

      <!-- 마스코트 클릭 시 메인으로 이동 / 마스코트 UI -->
      <a href="<%= request.getContextPath() %>/mainPage.jsp" class="bob-mascot">
        <img src="<%= request.getContextPath() %>/resource/images/mascot_open.png" alt="밥풀 마스코트">
        <div class="click-text">메인으로 돌아가기</div>
      </a>

      <div class="speech-bubble right">
        다시 시도해 주세요.
      </div>
    </div>
  </div>

</body>
</html>
