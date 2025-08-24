package com.babpool.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class ReviewDTO {

    private int reviewId;
    private int userId;
    private int storeId;
    private String content;
    private double rating;
    private Timestamp createdAt;

    // 수연 확장: 작성자 정보
    private String nickname;
    private String profileImagePath;
    // 수연 확장: 리뷰 이미지 정보
    private List<ReviewImageDTO> images = new ArrayList<>();

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
    
    public List<ReviewImageDTO> getImages() { return images; }
    public void setImages(List<ReviewImageDTO> images) { this.images = images; }

}
