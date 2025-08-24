<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
	<title>${store.name} <fmt:message key="placedetail.detailTitle" /></title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/placeDetail.css">
</head>
<body>

<%@ include file="headerFooter.jsp" %>

<section class="store-detail">

  <!-- 📸 사진 섹션 -->
  <section class="photo-section">
	<div class="photo-slider">
	  <c:forEach var="imgPath" items="${reviewImages}">
	    <img src="<%= request.getContextPath() %>/${imgPath}" alt="리뷰 이미지">
	  </c:forEach>
	</div>
  </section>

  <!-- 🏠 기본 정보 + 찜/카테고리 -->
  <section class="info-section">
    <div class="left-box">
      <div class="title-rating-box">
        <h2 class="store-name">${store.name}</h2>
        <div class="rating-box">
        
          <!-- ⭐ 이모티콘 조건문 삽입 -->
		  <span class="rating-stars">
		    <c:choose>
		      <c:when test="${ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
		      <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
		    </c:choose>
            <fmt:formatNumber value="${ratingAvg}" type="number" maxFractionDigits="1" />
          </span>
          
          <span class="rating-count">
			  (<fmt:message key="placedetail.peopleCount">
			    <fmt:param value="${totalCount}" />
			  </fmt:message>)
		  </span>

        </div>
      </div>
      <div class="store-meta">
		 <p><fmt:message key="placedetail.address"/>: ${store.address}</p>
		<p><fmt:message key="placedetail.openTime"/>: ${store.openTime}</p>
		<p><fmt:message key="placedetail.phone"/>: ${store.phone}</p>
      </div>

      <div class="menu-box">
        <h3><fmt:message key="placedetail.menuTitle" /></h3>
        <ul>
          <c:forEach var="menu" items="${menus}">
 			<li>${menu.name} - ${menu.price}<fmt:message key="placedetail.won"/></li>
          </c:forEach>
        </ul>
      </div>

      <div class="left-bottom-btn">
		<a class="direction-btn circle" href="${marker.url}" target="_blank">
		  <fmt:message key="placedetail.walk"/>
		</a>
      </div>
    </div>

    <div class="right-box">
	 <div class="like-container">
	  <form action="<%= request.getContextPath() %>/ToggleBookmarkServlet" method="post">
	    <input type="hidden" name="storeId" value="${store.storeId}">
	    <button type="submit" class="like-box ${isBookmarked ? 'active' : ''}">
	    <c:choose>
			<c:when test="${isBookmarked == true}">&#8203;❤️</c:when>
			<c:otherwise>&#8203;🤍</c:otherwise>
		</c:choose>

	    </button>
	  </form>
	  <span class="like-count">${likeCount}</span>
	 </div>


	 <div class="category-box">
	  <c:forEach var="category" items="${categories}">
	    <span>#<fmt:message key="category.${category.name}" /></span>
	  </c:forEach>
	</div>
	
	<div class="tag-box">
	  <c:forEach var="tag" items="${tags}">
	    <span>#<fmt:message key="tag.${tag.name}" /></span>
	  </c:forEach>
	</div>

    </div>
  </section>

  <!-- 📊 리뷰 요약 -->
  <section class="review-summary-section">
	<h3><fmt:message key="placedetail.reviewTitle" /></h3>
    <div class="review-summary-box">
    
    <!-- 별점 바 -->
      <div class="score-bar">
     	<c:forEach var="score" begin="1" end="5" step="1">
		  <c:set var="realScore" value="${6 - score}" />
		  <c:forEach var="entry" items="${scoreMap}">
		    <c:if test="${entry.key == realScore}">
		      <c:set var="count" value="${entry.value}" />
		    </c:if>
		  </c:forEach>
			<div>
			  <fmt:message key="placedetail.score"><fmt:param value="${realScore}"/></fmt:message> 
			  <div class="bar" style="width:${totalCount > 0 ? (count * 60) / totalCount : 0}%"></div> 
			  <fmt:message key="placedetail.count"><fmt:param value="${count}" /></fmt:message>
			</div>
		</c:forEach>
      </div>
      
       <!-- 오른쪽 별점 평균 영역 -->
    <div class="review-average">
      <div class="stars">
        <c:choose>
          <c:when test="${ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
          <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
        </c:choose>
      </div>
      <div class="score">
        <fmt:formatNumber value="${ratingAvg}" type="number" maxFractionDigits="1" />
      </div>
    </div>
      
    </div>
  </section>

  <!-- 🗣 리뷰 리스트 -->
  <section class="review-section">
    <div class="review-top-layout">
      <div class="review-row1">
        <p class="review-count-text">
		  <fmt:message key="placedetail.totalReviews">
		    <fmt:param value="${totalCount}" />
		  </fmt:message>
		</p>
      </div>
      <div class="review-row2">
        <form method="get" action="<%= request.getContextPath() %>/PlaceDetailServlet">
          <input type="hidden" name="storeId" value="${store.storeId}">
       <select name="sortType" onchange="this.form.submit()">
		  <option value="recent" ${sortType == 'recent' ? 'selected' : ''}><fmt:message key="placedetail.sort.recent"/></option>
		  <option value="high" ${sortType == 'high' ? 'selected' : ''}><fmt:message key="placedetail.sort.high"/></option>
		  <option value="low" ${sortType == 'low' ? 'selected' : ''}><fmt:message key="placedetail.sort.low"/></option>
		</select>
        </form>
        <button class="write-review-btn" onclick="location.href='writeReview.jsp?storeId=${store.storeId}'">
		  ✏️ <fmt:message key="placedetail.writeReview" />
		</button>
      </div>
    </div>

    <div class="review-list">
      <c:forEach var="review" items="${reviews}">
      
        <div class="review-item review-card">
          <div class="review-user">
            <img src="${pageContext.request.contextPath}${review.profileImagePath}" style="width:40px;height:40px;border-radius:50%;"> ${review.nickname}
          </div>
          <div class="review-date">${review.createdAt}</div>
          <div class="review-stars">${review.rating}점</div>
          <p class="review-text">${review.content}</p>
          
       	  <c:if test="${not empty review.images}">
        	<div class="review-images">
				<c:forEach var="img" items="${review.images}">
			      <img src="${pageContext.request.contextPath}/${img.imagePath}" />
			   	</c:forEach>
			</div>
		  </c:if>         
        </div>
        
      </c:forEach>
    </div>
  </section>

</section>

</body>
</html>