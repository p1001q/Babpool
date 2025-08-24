<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.net.URLEncoder" %>

<%
    // 파라미터로 오류 유형 받기
    String errorType = request.getParameter("errorType");
    String message = "알 수 없는 오류가 발생했습니다.";

    if ("noSession".equals(errorType)) {
        message = "로그인 정보가 확인되지 않습니다. 로그인 후 다시 시도해주세요.";
    } else if ("sessionExpired".equals(errorType)) {
        message = "세션 정보가 유효하지 않습니다. 다시 로그인해주세요.";
    } else if ("missingStoreId".equals(errorType)) {
        message = "잘못된 접근입니다. 가게 정보가 누락되었습니다.";
    } else if ("invalidParameterFormat".equals(errorType)) {
        message = "전달받은 데이터 형식이 잘못되었습니다. 새로고침 후 다시 시도해주세요.";
    } else if ("storeNotFound".equals(errorType)) {
        message = "해당 가게 정보를 찾을 수 없습니다. 삭제되었거나 존재하지 않는 가게입니다.";
    } else if ("invalidUserInfo".equals(errorType)) {
        message = "사용자 정보가 누락되었습니다. 다시 로그인 후 시도해주세요.";
    } else if ("missingRating".equals(errorType)) {
        message = "별점 정보가 누락되었습니다. 별점을 선택해주세요.";
    } else if ("missingReviewText".equals(errorType)) {
        message = "리뷰 내용이 누락되었습니다. 내용을 작성해주세요.";
    } else if ("fileSizeExceeded".equals(errorType)) {
        message = "첨부한 이미지가 용량 제한을 초과했습니다. 더 작은 파일을 선택해주세요.";
    } else if ("dbError".equals(errorType)) {
        message = "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>오류 발생</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f5eb;
            text-align: center;
            padding-top: 100px;
        }
        .error-box {
            display: inline-block;
            padding: 40px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 { color: #b71c1c; }
        p { color: #333; }
        button {
            margin-top: 20px;
            padding: 10px 20px;
            border: none;
            background-color: #b71c1c;
            color: white;
            border-radius: 5px;
            font-size: 1rem;
            cursor: pointer;
        }
        button:hover {
            background-color: #9c1b1b;
        }
    </style>
</head>
<body>

<div class="error-box">
    <h1>문제가 발생했습니다 😢</h1>
    <p><%= message %></p>
    <button onclick="history.back()">이전 페이지로</button>
</div>

</body>
</html>