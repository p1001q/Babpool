<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.sql.Connection" %>
<%@ page import="com.babpool.utils.DBUtil, com.babpool.utils.ApiKeyUtil" %>
<%@ page import="com.babpool.dao.CategoryDAO, com.babpool.dao.TagDAO, com.babpool.dao.MarkerDAO, com.babpool.dao.MarkerCategoryMapDAO, com.babpool.dao.MarkerTagMapDAO, com.babpool.dao.StoreDAO, com.babpool.dao.RecommendDAO" %>
<%@ page import="com.babpool.dto.CategoryDTO, com.babpool.dto.TagDTO, com.babpool.dto.MarkerDTO, com.babpool.dto.StoreDTO, com.babpool.dto.RecommendDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    // 언어 설정
    Locale locale = request.getLocale();
    String mapLang = locale.getLanguage();
    if (!mapLang.matches("ko|en|ja|zh")) mapLang = "ko";

    Connection conn = DBUtil.getConnection();
    
    // ✅ 추천메뉴 추천List도 불러오기 (기존 mainBabpool.jsp 에서 가져온 부분)
    RecommendDAO recommendDAO = new RecommendDAO(conn);
    List<RecommendDTO> recommendList = recommendDAO.getAllRecommendMenus();

    CategoryDAO categoryDAO = new CategoryDAO(conn);
    TagDAO tagDAO = new TagDAO(conn);
    MarkerDAO markerDAO = new MarkerDAO(conn);
    MarkerCategoryMapDAO markerCategoryDAO = new MarkerCategoryMapDAO(conn);
    MarkerTagMapDAO markerTagDAO = new MarkerTagMapDAO(conn);

    List<CategoryDTO> categoryList = categoryDAO.getAllCategories();
    List<TagDTO> tagList = tagDAO.getAllTags();
    List<MarkerDTO> markerList = new ArrayList<>();

    String categoryIdParam = request.getParameter("categoryId");
    String tagIdParam = request.getParameter("tagId");

    if (categoryIdParam != null) {
        int categoryId = Integer.parseInt(categoryIdParam);
        List<Integer> markerIds = markerCategoryDAO.getMarkerIdsByCategoryId(categoryId);
        for (int markerId : markerIds) {
            MarkerDTO marker = markerDAO.getMarkerById(markerId);
            if (marker != null) markerList.add(marker);
        }
    } else if (tagIdParam != null) {
        int tagId = Integer.parseInt(tagIdParam);
        List<Integer> markerIds = markerTagDAO.getMarkerIdsByTagId(tagId);
        for (int markerId : markerIds) {
            MarkerDTO marker = markerDAO.getMarkerById(markerId);
            if (marker != null) markerList.add(marker);
        }
    } else {
        markerList = markerDAO.getAllMarkers();
    }

    request.setAttribute("categoryList", categoryList);
    request.setAttribute("tagList", tagList);
    request.setAttribute("markerList", markerList);

    StoreDAO storeDAO = new StoreDAO(conn);
    List<Map<String, Object>> topStores = storeDAO.getTop3StoresWithCategory();
    request.setAttribute("topStores", topStores);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>BabPool - 메인 페이지</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/main.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/mainBabpool.css">
</head>
<body>
<%@ include file="/headerFooter.jsp" %>
  <div class="main-container-wrapper">
    
    <!-- 카테고리 영역 -->
    <aside class="category-list">
      <br>
      <a href="mainPage.jsp" class="category-link">
        <div class="category-item">
          <img src="<%= request.getContextPath() %>/resource/images/reset.png" />
          <span><fmt:message key="category.all" /></span>
        </div>
      </a>
    
      <div class="category-grid">
        <c:forEach var="cat" items="${categoryList}">
          <a href="mainPage.jsp?categoryId=${cat.categoryId}" class="category-link">
            <div class="category-item">
              <img src="<%= request.getContextPath() %>/resource/images/${cat.name}.png" />
              <span><fmt:message key="category.${cat.name}" /></span>
            </div>
          </a>
        </c:forEach>
      </div>
      <hr class="category-divider">
      <div class="category-grid sub">
        <c:forEach var="tag" items="${tagList}">
          <a href="mainPage.jsp?tagId=${tag.tagId}" class="category-link">
            <div class="category-item">
              <img src="<%= request.getContextPath() %>/resource/images/${tag.name}.png" />
              <span><fmt:message key="tag.${tag.name}" /></span>
            </div>
          </a>
        </c:forEach>
      </div>
    </aside>

    <!-- 지도 영역 -->
    <section class="map-section">
      <div id="map" style="width: 100%; height: 100%; border-radius: 12px;"></div>
      
      <script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=<%= ApiKeyUtil.get("ncpId") %>&language=<%= mapLang %>"></script>
		
      <script>
        const markerList = [
          <c:forEach var="m" items="${markerList}" varStatus="loop">
            { 
              lat: ${m.wgsY}, 
              lng: ${m.wgsX}, 
              storeId: ${m.storeId}, 
              storeName: "${fn:replace(m.storeName, '\"', '\\\\"')}"
            }<c:if test="${!loop.last}">,</c:if>
          </c:forEach>
        ];

        let map;
        let markers = [];

        document.addEventListener("DOMContentLoaded", function () {
          map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(37.6154, 127.0115),
            zoom: 17
          });
          drawMarkers(markerList);
        });

        function drawMarkers(data) {
          clearMarkers();
          data.forEach(marker => {
            const m = new naver.maps.Marker({
              position: new naver.maps.LatLng(marker.lat, marker.lng),
              map: map
            });

            const infoWindow = new naver.maps.InfoWindow({ content: '<div style="padding:5px;">' + marker.storeName + '</div>' });
            
            naver.maps.Event.addListener(m, "mouseover", () => infoWindow.open(map, m));
            naver.maps.Event.addListener(m, "mouseout", () => infoWindow.close());
            naver.maps.Event.addListener(m, "click", function () {
                window.open('<%= request.getContextPath() %>/PlaceDetailServlet?storeId=' + marker.storeId, '_blank');
            });

            markers.push(m);
          });
        }

        function clearMarkers() {
          markers.forEach(m => m.setMap(null));
          markers = [];
        }
      </script>
    </section>

    <!-- Top3 영역 -->
    <aside class="top-list">
      <h3><fmt:message key="main.top3Title"/></h3>
      
      <c:forEach var="store" items="${topStores}">
        <div class="top-card">
          <a href="<%= request.getContextPath() %>/PlaceDetailServlet?storeId=${store.storeId}" class="top-link">
            <div class="top-info">
              <div class="top-left">
                <img src="<%= request.getContextPath() %>/resource/images/${store.categoryName}.png" alt="${store.categoryName}">
              </div>
              <div class="top-right">
                <div class="top-name">${store.name}</div>
                <div class="stars">
                  <c:choose>
                    <c:when test="${store.ratingAvg <= 1.0}">☆ ☆ ☆ ☆ ☆</c:when>
                    <c:when test="${store.ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
                    <c:when test="${store.ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
                    <c:when test="${store.ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
                    <c:when test="${store.ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
                    <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
                  </c:choose>
                </div>

                <div class="top-like">
                  <span class="heart">❤️</span>
                  <span>${store.likeCount}</span>
                </div>

                <div class="top-tag">#<fmt:message key="category.${store.categoryName}" /></div>
              </div>
            </div>
          </a>
        </div>
      </c:forEach>
    </aside>

  </div>

  <!-- 마스코트 영역 (수연 확장 적용) -->
  <div class="mascot-area" id="mainMascot">
    <img src="<%= request.getContextPath() %>/resource/images/mascot_close.png" alt="마스코트" onclick="openHelp()">
  </div>


<!-- ✅ 검정 오버레이 -->
<div id="helpOverlay" onclick="closeHelp()"></div>

<!-- ✅ 1단계 : 처음 질문 모달 -->
<div id="mascotModal">
    <p>어떻게 도와줄까?</p>
    <button onclick="recommendNearby()">주변 밥집 추천해줘</button>
    <button onclick="recommendMenu()">메뉴만 추천해줘</button>
    <button onclick="closeHelp()">괜찮아</button>
</div>

<!-- ✅ 2단계 : 추천 결과 모달 -->
<div id="recommendPlace">
    <p>여기 어때?</p>

   <!-- 상단: 가게 이름 / 별점 / 하트 -->
   <div class="recommend-header">
       <span id="storeName"></span>
       <span id="ratingStars"></span>
       <span id="ratingAvg"></span>
       <span class="recommend-like">❤️ <span id="likeCount"></span></span>
   </div>
   
   <!-- 3분할 영역: 사진 / 리뷰 / 태그 -->
   <div class="recommend-body">
       <!-- 왼쪽: 음식 사진 -->
    <div class="recommend-image">
        <img id="storeImage" src="" alt="대표사진" onerror="handleNoImage()">
        <p id="noImageText" style="display: none; color: gray; font-size: 14px; margin-top: 8px;">
            사진 리뷰가<br>
            등록되지 않은<br>
            가게에요. 😥
        </p>
    </div>

       <!-- 가운데: 리뷰 -->
       <div class="recommend-reviews" id="reviewList"></div>
   
       <!-- 오른쪽: 태그 -->
       <div class="tag-box" id="tagList"></div>
   </div>
   
   
       <!-- 하단 버튼 3개 -->
       <div class="recommend-buttons">
           <button class="goStoreBtn" onclick="goStorePlace()">O 좋다! 가게 보러갈래</button>
           <button class="nextBtn" onclick="recommendNearby()">X 다른 가게 추천해줘</button>
           <button class="closeBtn" onclick="closeHelp()">추천 그만 받을래</button>
       </div>
   </div>

<!-- ✅ 2단계 : 메뉴 추천 모달 -->
<div id="recommendMenu" style="display:none;">

    <div class="menu-filter-buttons">
        <button class="typeBtn" data-type="1">한식</button>
        <button class="typeBtn" data-type="2">양식</button>
        <button class="typeBtn" data-type="3">중식</button>
        <button class="typeBtn" data-type="4">일식</button>
        <button class="typeBtn" data-type="5">그외</button>
    </div>

    <div class="menu-result">
        <h2 id="menuName">추천 메뉴명</h2><span>어때?</span>
    </div>

    <div class="recommend-buttons">
       <button class="goStoreBtn" onclick="goStoreMenu()">O 좋다! 골라줘서 고마워</button>
        <button class="nextBtn" onclick="randomMenu()">X 다른 메뉴 추천해줘</button>
        <button class="closeBtn" onclick="closeHelp()">추천 그만 받을래</button>
    </div>
</div>


<!-- ✅ 멘트 전용 메시지 박스 -->
<div id="ByeMessage" style="display:none;">
    <p id="messageText" style="font-size:1.2rem; text-align:center; margin:20px;"></p>
</div>

<!-- ✅ 밥풀이 이미지 (모달 위로 띄우기) -->
<div id="mascotCharacter">
    <img src="<%= request.getContextPath() %>/resource/images/mascot_open.png" alt="밥풀이">
</div>

<script>
let currentStoreId = null;  // ⭐ 현재 추천받은 가게 id 저장용
const contextPath = "<%= request.getContextPath() %>";


const recommendList = [
   <% for (RecommendDTO rec : recommendList) { %>
       { type: <%= rec.getType() %>, name: "<%= rec.getName() %>" },
   <% } %>
];


let selectedTypes = [1, 2, 3, 4, 5];

function openHelp() {
    document.getElementById('helpOverlay').style.display = 'block';
    document.getElementById('mascotModal').style.display = 'block';
    document.getElementById('mascotCharacter').style.display = 'block';
    document.getElementById('recommendPlace').style.display = 'none';
    document.getElementById('recommendMenu').style.display = 'none';
    document.getElementById('ByeMessage').style.display = 'none';
    document.getElementById('mainMascot').style.display = 'none';
}

//✅ 가게 추천 데이터 불러오기 (완전 안정화버전)
function recommendNearby() {
    document.getElementById("recommendPlace").style.display = "block";
    document.getElementById("ByeMessage").style.display = "none";
    document.querySelector(".goStoreBtn").disabled = true;

    fetch("RandomStoreServlet")
    .then(response => response.json())
    .then(data => {
        console.log("받은 데이터:", data);

        // 가게이름 (⭐ textContent 로 안정화)
        document.getElementById("storeName").textContent = data.storeName;

        // 별점 수치 (⭐ 안정화)
        const avg = parseFloat(data.ratingAvg);
        document.getElementById("ratingAvg").textContent = "(" + avg.toFixed(1) + ")";

        // 별점 이모지
        let stars = "⭐⭐⭐⭐⭐";
        if (avg <= 1.0) stars = "☆ ☆ ☆ ☆ ☆";
        else if (avg <= 1.5) stars = "⭐☆ ☆ ☆ ☆";
        else if (avg <= 2.5) stars = "⭐⭐ ☆ ☆ ☆";
        else if (avg <= 3.5) stars = "⭐⭐⭐ ☆ ☆";
        else if (avg <= 4.5) stars = "⭐⭐⭐⭐ ☆";
        document.getElementById("ratingStars").textContent = stars;

        // 찜수
        document.getElementById("likeCount").textContent = data.likeCount;

        // ⭐ 리뷰 안정화 렌더링 (JS로 title 포함)
        const reviewList = document.getElementById("reviewList");
        reviewList.innerHTML = ""; // 초기화
        const reviewTitle = document.createElement("div");
        reviewTitle.className = "review-section-title";
        reviewTitle.textContent = "풀잎이들의 리뷰";
        reviewList.appendChild(reviewTitle);

        const reviews = (data.reviews || []).filter(r => r && r.trim() !== "");
        if (reviews.length === 0) {
            const emptyDiv = document.createElement("div");
            emptyDiv.className = "review-box";
            emptyDiv.textContent = "아직 리뷰가 없습니다";
            reviewList.appendChild(emptyDiv);
        } else {
            reviews.forEach(review => {
                const reviewBox = document.createElement("div");
                reviewBox.className = "review-box";
                const span = document.createElement("span");
                span.textContent = review;
                reviewBox.appendChild(span);
                reviewList.appendChild(reviewBox);
            });
        }

        // ⭐ 태그 안정화 렌더링
        const tagList = document.getElementById("tagList");
        tagList.innerHTML = ""; // 초기화
        const tags = (data.tags || []).filter(t => t && t.trim() !== "");
        if (tags.length === 0) {
            const emptyTag = document.createElement("span");
            emptyTag.textContent = "태그 없음";
            tagList.appendChild(emptyTag);
        } else {
            tags.forEach(tag => {
                const tagSpan = document.createElement("span");
                tagSpan.textContent = "#" + tag;
                tagList.appendChild(tagSpan);
            });
        }

        // 이미지
        document.getElementById("storeImage").src = data.imagePath;

        // storeId 저장
        currentStoreId = parseInt(data.storeId);
        console.log("현재 저장된 storeId:", currentStoreId, typeof currentStoreId);
        document.querySelector(".goStoreBtn").disabled = false;
    })
    .catch(err => console.error("가게 추천 오류:", err));

    document.getElementById("mascotModal").style.display = "none";
    document.getElementById("recommendPlace").style.display = "block";
}

function goStorePlace() {
    if (!currentStoreId || isNaN(currentStoreId)) {
        alert("가게 정보를 아직 불러오는 중입니다. 잠시만 기다려 주세요.");
        return;
    }

    const storeIdForRedirect = currentStoreId;

    document.getElementById("recommendPlace").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "내가 도움이 됐다니 기쁘다! 밥 맛있게 먹어!";

    setTimeout(function() {
        console.log("이동 직전 storeIdForRedirect:", storeIdForRedirect);
        window.location.href = "PlaceDetailServlet?storeId=" + currentStoreId;
    }, 1500);
}

function goStoreMenu() {
    document.getElementById("recommendMenu").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "내가 도움이 됐다니 기쁘다! 밥 맛있게 먹어!";

    setTimeout(function() {
        document.getElementById('helpOverlay').style.display = 'none';
        document.getElementById('ByeMessage').style.display = 'none';
        document.getElementById('mascotCharacter').style.display = 'none';
        document.getElementById('mainMascot').style.display = 'block';
    }, 1500);
}

function closeHelp() {
    document.getElementById("mascotModal").style.display = "none";
    document.getElementById("recommendPlace").style.display = "none";
    document.getElementById("recommendMenu").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "다시 도움이 필요하면 날 눌러줘!";

    setTimeout(function() {
        document.getElementById('helpOverlay').style.display = 'none';
        document.getElementById('ByeMessage').style.display = 'none';
        document.getElementById('mascotCharacter').style.display = 'none';
        document.getElementById('mainMascot').style.display = 'block';
    }, 1500);
}

function recommendMenu() {
    randomMenu();
    document.getElementById('mascotModal').style.display = 'none';
    document.getElementById('recommendMenu').style.display = 'block';

    document.querySelectorAll(".typeBtn").forEach(btn => {
        const type = parseInt(btn.dataset.type);
        if (selectedTypes.includes(type)) {
            btn.classList.add("active");
        } else {
            btn.classList.remove("active");
        }
    });
}

function randomMenu() {
    const filtered = recommendList.filter(menu => selectedTypes.includes(menu.type));
    const randomIndex = Math.floor(Math.random() * filtered.length);
    const selected = filtered[randomIndex];
    document.getElementById("menuName").innerText = selected.name;
}

function handleNoImage() {
    const img = document.getElementById("storeImage");
    const text = document.getElementById("noImageText");

    if (img) img.style.display = "none";
    if (text) text.style.display = "block";
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".typeBtn").forEach(btn => {
        btn.addEventListener("click", () => {
            const type = parseInt(btn.dataset.type);
            if (selectedTypes.includes(type)) {
                selectedTypes = selectedTypes.filter(t => t !== type);
                btn.classList.remove("active");
            } else {
                selectedTypes.push(type);
                btn.classList.add("active");
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("mascot") === "true") {
        openHelp();
    }
});
</script>


</body>
</html>