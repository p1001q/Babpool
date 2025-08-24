package com.babpool.dto;

public class RecommendDTO {
    private int type;
    private String name;

    // 생성자
    public RecommendDTO() {}

    public RecommendDTO(int type, String name) {
        this.type = type;
        this.name = name;
    }

    // getter/setter
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
