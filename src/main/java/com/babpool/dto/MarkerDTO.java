package com.babpool.dto;

import java.sql.Timestamp;

public class MarkerDTO {
    private int markerId;
    private int storeId;
    private int placeId;
    private String storeName;
    private double wgsX;
    private double wgsY;
    private double tmX;
    private double tmY;
    private String url;
    private String unicodeName;
    private Timestamp createdAt;

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getWgsX() {
        return wgsX;
    }

    public void setWgsX(double wgsX) {
        this.wgsX = wgsX;
    }

    public double getWgsY() {
        return wgsY;
    }

    public void setWgsY(double wgsY) {
        this.wgsY = wgsY;
    }

    public double getTmX() {
        return tmX;
    }

    public void setTmX(double tmX) {
        this.tmX = tmX;
    }

    public double getTmY() {
        return tmY;
    }

    public void setTmY(double tmY) {
        this.tmY = tmY;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUnicodeName() {
        return unicodeName;
    }

    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
}