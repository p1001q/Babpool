<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*, com.babpool.dao.*, com.babpool.dto.*, com.babpool.utils.DBUtil, java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <title>가게 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/manageStore.css" />
</head>
<body>
    <div class="manage-container">
        <h2>가게 관리</h2>
        
        <div style="text-align: right; margin: 10px 20px; font-size: 0.85rem;">
	        <a href="<%=request.getContextPath()%>/adminMainPage.jsp" style="color: #4CAF50; text-decoration: none;">관리자 메인으로 이동</a>
	    </div>

        <div class="form-container">
            <!-- 좌측: 음식점 영역 -->
            <div class="left-section">
                <h3>가게 정보</h3>
                
				<h4>가게 전체 조회</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreSelectAllServlet" method="get">
                    <input type="hidden" name="toggle" value="true" />
                    <button type="submit" class="btn">가게 전체 조회</button>
                </form>

				<h4>가게 선택 조회</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreSelectByIdServlet" method="get">
                    <input type="number" name="storeId" placeholder="가게 ID 입력" required />
                    <button type="submit" class="btn">가게 선택 조회</button>
                </form>

				<h4>가게 수정</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreInsertServlet" method="post">
                    <input type="text" name="name" placeholder="가게 이름" required />
                    <input type="text" name="address" placeholder="주소" required />
                    <input type="text" name="phone" placeholder="전화번호" required />
                    <input type="text" name="openTime" placeholder="운영시간" required />
                    <button type="submit" class="btn">가게 등록</button>
                </form>
                
                <c:if test="${not empty storeInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeInsertResult}">
				                ✅ 가게 등록 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 가게 등록 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
				<h4>가게 수정</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreUpdateServlet" method="post">
                    <input type="number" name="storeId" placeholder="수정할 가게 ID" required />
                    <input type="text" name="name" placeholder="가게 이름 수정" />
                    <input type="text" name="address" placeholder="주소 수정" />
                    <input type="text" name="phone" placeholder="전화번호 수정" />
                    <input type="text" name="openTime" placeholder="운영시간 수정" />
                    <button type="submit" class="btn">가게 수정</button>
                </form>
                
                <c:if test="${not empty storeUpdateResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeUpdateResult}">
				                ✅ 가게 수정 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 가게 수정 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
    			
    			<h4>가게 삭제</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreDeleteServlet" method="post">
                    <input type="number" name="storeId" placeholder="삭제할 가게 ID" required />
                    <button type="submit" class="btn">가게 삭제</button>
                </form>
                
                <c:if test="${not empty storeDeleteResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeDeleteResult}">
				                ✅ 가게 삭제 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 가게 삭제 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <h4>가게-카테고리 연결</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreCategoryInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="number" name="categoryId" placeholder="카테고리 ID" required />
                    <button type="submit" class="btn">가게 - 카테고리 연결</button>
                </form>

                <h4>가게-태그 연결</h4>
                <form action="<%=request.getContextPath()%>/AdminStoreTagInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="number" name="tagId" placeholder="태그 ID" required />
                    <button type="submit" class="btn">가게 - 태그 연결</button>
                </form>

				<br>
				
				<h3>메뉴 정보</h3>
				
				<h4>메뉴 전체 조회</h4>
				<form action="<%=request.getContextPath()%>/AdminMenuSelectAllServlet" method="get">
                    <input type="hidden" name="toggle" value="true" />
                    <button type="submit" class="btn">메뉴 전체 조회</button>
                </form>


                <!-- 메뉴 등록 -->
                <h4>메뉴 등록</h4>
                <form action="<%=request.getContextPath()%>/AdminMenuInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="text" name="name" placeholder="메뉴 이름" required />
                    <input type="number" name="price" placeholder="가격" required />
                    <button type="submit" class="btn">메뉴 등록</button>
                </form>
                
                <c:if test="${not empty menuInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuInsertResult}">
				                ✅ 메뉴 추가 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 추가 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                

                <!-- 메뉴 수정 -->
                <h4>메뉴 수정</h4>
                <form action="<%=request.getContextPath()%>/AdminMenuUpdateServlet" method="post">
                    <input type="number" name="menuId" placeholder="메뉴 ID" required />
                    <input type="text" name="name" placeholder="메뉴 이름 수정" />
                    <input type="number" name="price" placeholder="가격 수정" />
                    <button type="submit" class="btn">메뉴 수정</button>
                </form>
                
                <c:if test="${not empty menuUpdateResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuUpdateResult}">
				                ✅ 메뉴 수정 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 수정 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                

                <!-- 메뉴 삭제 -->
                <h4>메뉴 삭제</h4>
                <form action="<%=request.getContextPath()%>/AdminMenuDeleteServlet" method="post">
                    <input type="number" name="menuId" placeholder="메뉴 ID" required />
                    <button type="submit" class="btn">메뉴 삭제</button>
                </form>
                
                <c:if test="${not empty menuDeleteResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuDeleteResult}">
				                ✅ 메뉴 삭제 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 삭제 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                <c:if test="${not empty storeList}">
                    <h4>전체 가게 목록</h4>
                    <table class="simple-table">
                        <tr><th>ID</th><th>이름</th><th>주소</th><th>전화번호</th></tr>
                        <c:forEach var="s" items="${storeList}">
                            <tr>
                                <td>${s.storeId}</td>
                                <td>${s.name}</td>
                                <td>${s.address}</td>
                                <td>${s.phone}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                
                <c:if test="${not empty store}">
				    <h4>선택 가게 조회 결과</h4>
				    <table class="simple-table">
				        <tr><th>ID</th><th>이름</th><th>주소</th><th>전화번호</th><th>운영시간</th></tr>
				        <tr>
				            <td>${store.storeId}</td>
				            <td>${store.name}</td>
				            <td>${store.address}</td>
				            <td>${store.phone}</td>
				            <td>${store.openTime}</td>
				        </tr>
				    </table>
				</c:if>
				<c:if test="${storeNotFound}">
				    <p style="color:red;">해당 ID의 음식점이 존재하지 않습니다.</p>
				</c:if>
                
                <c:if test="${not empty menuList}">
				    <h4>전체 메뉴 목록</h4>
				    <table class="simple-table">
				        <tr><th>ID</th><th>가게 ID</th><th>이름</th><th>가격</th></tr>
				        <c:forEach var="m" items="${menuList}">
				            <tr>
				                <td>${m.menuId}</td>
				                <td>${m.storeId}</td>
				                <td>${m.name}</td>
				                <td>${m.price}</td>
				            </tr>
				        </c:forEach>
				    </table>
				</c:if>
                
                
                
            </div>

            <!-- 우측: 마커 영역 -->
            <div class="right-section">
                <h3>마커 정보</h3>
                
				<h4>마커 전체 조회</h4>
                <form action="<%=request.getContextPath()%>/AdminMarkerSelectAllServlet" method="get">
                    <input type="hidden" name="toggle" value="true" />
                    <button type="submit" class="btn">마커 전체 조회</button>
                </form>

				<h4>마커 선택 조회</h4>
                <form action="<%=request.getContextPath()%>/AdminMarkerSelectByStoreIdServlet" method="get">
                    <input type="number" name="storeId" placeholder="마커 ID 입력" required />
                    <button type="submit" class="btn">마커 선택 조회</button>
                </form>

                <%-- <h4>장소 좌표 등록</h4>
                <form action="<%=request.getContextPath()%>/AdminMarkerInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="text" name="storeName" placeholder="가게 이름" required />
                    <input type="text" name="placeId" placeholder="네이버 장소 ID" required />
                    <input type="text" name="wgsX" placeholder="WGS X" required />
                    <input type="text" name="wgsY" placeholder="WGS Y" required />
                    <input type="text" name="tmX" placeholder="TM X" required />
                    <input type="text" name="tmY" placeholder="TM Y" required />
                    <input type="text" name="unicode" placeholder="가게이름 유니코드" />
                    <button type="submit" class="btn">마커 추가 및 길찾기 URL 추가</button>
                </form> --%>
                
                <h4>마커 등록</h4>
				<form action="<%=request.getContextPath()%>/AdminMarkerInsertServlet" method="post">
				    <input type="number" name="storeId" placeholder="가게 ID" required />
				    <input type="number" name="placeId" placeholder="네이버 장소 ID" required />
				    <button type="submit" class="btn">마커 등록</button>
				</form>
                
                
                
                <c:if test="${not empty markerInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerInsertResult}">
				                ✅ 마커 추가 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커 추가 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                <h4>마커-카테고리 연결</h4>
                <form action="<%=request.getContextPath()%>/AdminMarkerCategoryInsertServlet" method="post">
                    <input type="number" name="markerId" placeholder="마커 ID" required />
                    <input type="number" name="categoryId" placeholder="카테고리 ID" required />
                    <button type="submit" class="btn">카테고리 연결</button>
                </form>
                
                <c:if test="${not empty markerCategoryInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerCategoryInsertResult}">
				                ✅ 마커-카테고리 연결 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커-카테고리 연결 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                <h4>마커-태그 연결</h4>
                <form action="<%=request.getContextPath()%>/AdminMarkerTagInsertServlet" method="post">
                    <input type="number" name="markerId" placeholder="마커 ID" required />
                    <input type="number" name="tagId" placeholder="태그 ID" required />
                    <button type="submit" class="btn">태그 연결</button>
                </form>
                
                <c:if test="${not empty markerTagInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerTagInsertResult}">
				                ✅ 마커-태그 연결 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커-태그 연결 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
               
                <c:if test="${not empty markerList}">
				    <h4>전체 마커 목록</h4>
				    <table class="simple-table">
				        <tr>
				            <th>마커ID</th>
				            <th>가게ID</th>
				            <th>가게명</th>
				            <th>WGS</th>
				            <th>TM</th>
				            <th>URL</th> <!-- ✅ 추가 -->
				        </tr>
				        <c:forEach var="m" items="${markerList}">
				            <tr>
				                <td>${m.markerId}</td>
				                <td>${m.storeId}</td>
				                <td>${m.storeName}</td>
				                <td>(${m.wgsX}, ${m.wgsY})</td>
				                <td>(${m.tmX}, ${m.tmY})</td>
				                <td>
				                    <a href="${m.url}" target="_blank">길찾기</a> <!-- ✅ 링크로 출력 -->
				                </td>
				            </tr>
				        </c:forEach>
				    </table>
				</c:if>

                
                <c:if test="${not empty marker}">
				    <h4>선택 마커 조회 결과</h4>
				    <table class="simple-table">
				        <tr>
				            <th>마커ID</th>
				            <th>스토어ID</th>
				            <th>스토어명</th>
				            <th>TM 좌표</th>
				            <th>WGS 좌표</th>
				            <th>URL</th> <!-- ✅ 추가 -->
				        </tr>
				        <tr>
				            <td>${marker.markerId}</td>
				            <td>${marker.storeId}</td>
				            <td>${marker.storeName}</td>
				            <td>(${marker.wgsX}, ${marker.wgsY})</td>
				            <td>(${marker.tmX}, ${marker.tmY})</td>
				            <td>
				                <a href="${marker.url}" target="_blank">길찾기</a> <!-- ✅ 링크로 출력 -->
				            </td>
				        </tr>
				    </table>
				</c:if>

				
				<c:if test="${markerNotFound}">
				    <p style="color:red;">해당 ID의 마커가 존재하지 않습니다.</p>
				</c:if>

                <!-- ✅ 항상 출력되는 카테고리/태그 테이블 -->
                <%
                    Connection conn = DBUtil.getConnection();
                    CategoryDAO cdao = new CategoryDAO(conn);
                    TagDAO tdao = new TagDAO(conn);
                    List<CategoryDTO> categoryList = cdao.getAllCategories();
                    List<TagDTO> tagList = tdao.getAllTags();
                    conn.close();
                %>

                <h4>카테고리 목록</h4>
                <table class="simple-table">
                    <tr><th>ID</th><th>카테고리명</th></tr>
                    <c:forEach var="c" items="<%= categoryList %>">
                        <tr><td>${c.categoryId}</td><td>${c.name}</td></tr>
                    </c:forEach>
                </table>

                <h4>태그 목록</h4>
                <table class="simple-table">
                    <tr><th>ID</th><th>태그명</th></tr>
                    <c:forEach var="t" items="<%= tagList %>">
                        <tr><td>${t.tagId}</td><td>${t.name}</td></tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
 