<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.sql.*, com.babpool.dao.*, com.babpool.dto.*, com.babpool.utils.DBUtil, java.util.*" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />

<%
    // 세션에서 로그인 사용자 가져오기
    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

if (loginUser == null) {
%>
	<script>
	    alert("<fmt:message key='common.loginRequired'/>");
	    location.href = "login.jsp";
	</script>
<%
    return;
}
%>

<%
// ✅ 추가로 찜 가게 / 내가 쓴 리뷰 / 프로필 이미지 조회 (동국)
Connection conn = null;
UserDAO userDAO = null;
ReviewDAO reviewDAO = null;
BookmarkDAO bookmarkDAO = null;
StoreDAO storeDAO = null;

UserDTO user = null;
List<ReviewDTO> reviews = null;
List<BookmarkDTO> bookmarks = null;
List<StoreDTO> stores = new ArrayList<>();

try {
    conn = DBUtil.getConnection();
    userDAO = new UserDAO(conn);
    reviewDAO = new ReviewDAO(conn);
    bookmarkDAO = new BookmarkDAO(conn);
    storeDAO = new StoreDAO(conn);

    // DB에서 최신 유저 정보 가져오기 (프로필 이미지 최신화용)
    user = userDAO.getUserByEmail(loginUser.getEmail());

    // 찜한 가게, 내가 쓴 리뷰 조회
    reviews = reviewDAO.getReviewsByUserId(user.getUserId());
    bookmarks = bookmarkDAO.getBookmarkedStoresByUserId(user.getUserId());

    for (BookmarkDTO bookmark : bookmarks) {
        StoreDTO store = storeDAO.getStoreById(bookmark.getStoreId());
        stores.add(store);
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    try {
        if (conn != null) conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="mypage.title"/></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/myPage.css">
</head>
<body>

<%@ include file="/headerFooter.jsp" %>

<main class="mypage-container">
    <section class="profile-section">
        <div class="profile-image-div">
	        <form action="<%=request.getContextPath()%>/UploadProfileServlet" method="post" enctype="multipart/form-data">
			    <label for="profileImage">
			        <img id="profilePreview" 
			             src="<%= request.getContextPath() + (loginUser != null && loginUser.getProfileImagePath() != null 
			                 ? loginUser.getProfileImagePath() 
			                 : "/resource/images/profile/default_profile.png") %>" 
			             class="profile-img" alt="프로필 이미지">
			    </label>
			    <input type="file" id="profileImage" name="profileImage" accept="image/*" onchange="previewImage(event)">
			    <button type="submit"><fmt:message key="mypage.changeProfile"/></button>
			</form>
        </div>
        <div class="user-info">
            <p class="nickname"><%= loginUser.getNickname() %></p>
            <p class="email"><%= loginUser.getEmail() %></p>
        </div>
        
        <div class="profile-buttons">    
		    <button type="button" onclick="location.href='<%= request.getContextPath() %>/LogoutServlet'"><fmt:message key="mypage.logout"/></button>
		    <button type="button" onclick="location.href='changePasswd.jsp'"><fmt:message key="mypage.changePassword"/></button>
		
		    <!-- form은 이렇게 바꿔주자 -->
		    <form action="<%=request.getContextPath()%>/DeleteUserServlet" method="post" style="width: 100%;">
		        <button type="submit" style="width: 100%;"><fmt:message key="mypage.withdraw"/></button>
		    </form>
		</div>

    </section>
	
	<section class="favorite-section">
    <h3><fmt:message key="mypage.favorite"/></h3>
    <div class="favorite-list">
        <% for (StoreDTO store : stores) { %>
            <div class="favorite-item">
                <a href="<%= request.getContextPath() %>/PlaceDetailServlet?storeId=<%= store.getStoreId() %>">
                    <p><%= store.getName() %></p>
                    
                    <!-- 별점 출력 부분 -->
                    <div class="rating-stars">
                        <%
                            float ratingAvg = store.getRatingAvg();
                            String stars = "";
                            
                            if (ratingAvg <= 1.5) {
                                stars = "⭐☆ ☆ ☆ ☆";
                            } else if (ratingAvg <= 2.5) {
                                stars = "⭐⭐☆ ☆ ☆";
                            } else if (ratingAvg <= 3.5) {
                                stars = "⭐⭐⭐☆ ☆";
                            } else if (ratingAvg <= 4.5) {
                                stars = "⭐⭐⭐⭐☆";
                            } else {
                                stars = "⭐⭐⭐⭐⭐";
                            }
                        %>
                        <%= stars %>
                    </div>
                    
                    <!-- 평점 수치 출력 -->
                    <div class="rating-score">
                        <%= String.format("%.1f", ratingAvg) %>
                    </div>
                </a>
            </div>
        <% } %>
    </div>
</section>

	
    

    <section class="review-section">
        <h3><fmt:message key="mypage.myReviews"/></h3>
        <%
        storeDAO = null;
        storeDAO = new StoreDAO(DBUtil.getConnection());  // StoreDAO 객체 생성
        for (ReviewDTO review : reviews) { 
            // storeId를 통해 storeName을 가져옵니다.
            String storeName = storeDAO.getName(review.getStoreId());  
            // storeName이 null인 경우 기본 값을 설정
            if (storeName == null) {
                storeName = "알 수 없음"; // 기본값 설정
            }
    %>
        <div class="review-item">
            <p> - <!-- 별점 출력 부분 -->
            <a href="<%= request.getContextPath() %>/PlaceDetailServlet?storeId=<%= review.getStoreId() %>"><%= storeName %></a>
		<span class="rating-stars">
		    <%
		        double rating = review.getRating();  // review.getRating()을 사용
		        String stars = "";
		
		        // 별점 조건에 따른 출력
		        if (rating <= 1.5) {
		            stars = "⭐☆ ☆ ☆ ☆";
		        } else if (rating <= 2.5) {
		            stars = "⭐⭐☆ ☆ ☆";
		        } else if (rating <= 3.5) {
		            stars = "⭐⭐⭐☆ ☆";
		        } else if (rating <= 4.5) {
		            stars = "⭐⭐⭐⭐☆";
		        } else {
		            stars = "⭐⭐⭐⭐⭐";
		        }
		    %>
		    <%= stars %>
		
		    <!-- 평점 출력 (소수점 1자리까지) -->
		    <%= String.format("%.1f", rating) %>
		</span> <%= review.getContent() %></p>
            <!-- 삭제 버튼: 해당 리뷰 ID를 ReviewDeleteServlet으로 전송 -->
            <form action="<%=request.getContextPath()%>/ReviewDeleteServlet" method="post">
                <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
                <button type="submit"><fmt:message key="mypage.delete"/></button>
            </form>
        </div>
        
    <% } %>
    </section>
</main>

<script>
function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function(){
        document.getElementById('profilePreview').src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}
</script>
</body>
</html> 