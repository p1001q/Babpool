-- 0단계 테이블 삭제
-- 💡 우선 외래키 체크 비활성화 (임시로)
SET FOREIGN_KEY_CHECKS = 0;

-- 삭제 순서: 하위 테이블 → 상위 테이블
DROP TABLE IF EXISTS recommend;
DROP TABLE IF EXISTS marker_tag_map;
DROP TABLE IF EXISTS marker_category_map;
DROP TABLE IF EXISTS marker;
DROP TABLE IF EXISTS store_tag_map;
DROP TABLE IF EXISTS store_category_map;
DROP TABLE IF EXISTS bookmark;
DROP TABLE IF EXISTS review_image;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS user;

-- 외래키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

-- 1단계: 테이블 생성
-- 사용자 테이블: 회원 로그인/정보 저장
CREATE TABLE user (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  nickname VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  profile_image_path VARCHAR(255) DEFAULT "/resource/images/profile/default_profile.png"
);

-- 음식점 테이블: 기본 정보 + 즐겨찾기 수 등
CREATE TABLE store (
  store_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  address VARCHAR(255) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  open_time VARCHAR(50) NOT NULL,
  rating_avg FLOAT DEFAULT 0.0,
  review_count INT DEFAULT 0,
  like_count INT DEFAULT 0  -- 즐겨찾기 수 (bookmark 기반 캐시)
);

-- 카테고리 테이블: 한식, 중식 등 분류
CREATE TABLE category (
  category_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

-- 태그 테이블: 혼밥, 배달, 단체석 등
CREATE TABLE tag (
  tag_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

-- 메뉴 테이블: 음식점별 메뉴 목록
CREATE TABLE menu (
  menu_id INT PRIMARY KEY AUTO_INCREMENT,
  store_id INT,
  name VARCHAR(100) NOT NULL,
  price INT NOT NULL,
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- 리뷰 테이블: 사용자 리뷰 저장
CREATE TABLE review (
  review_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  store_id INT,
  content TEXT NOT NULL,
  rating FLOAT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- 리뷰 이미지 테이블: 다중 이미지 저장
CREATE TABLE review_image (
  image_id INT PRIMARY KEY AUTO_INCREMENT,
  review_id INT,
  image_path VARCHAR(255) NOT NULL,
  image_order INT DEFAULT 0,
  FOREIGN KEY (review_id) REFERENCES review(review_id) ON DELETE CASCADE
);

-- 즐겨찾기 테이블: 사용자 ↔ 음식점 다대다 관계
CREATE TABLE bookmark (
  user_id INT,
  store_id INT,
  PRIMARY KEY (user_id, store_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- 음식점-카테고리 매핑 테이블 (다대다)
CREATE TABLE store_category_map (
  store_id INT,
  category_id INT,
  PRIMARY KEY (store_id, category_id),
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE
);

-- 음식점-태그 매핑 테이블 (다대다)
CREATE TABLE store_tag_map (
  store_id INT,
  tag_id INT,
  PRIMARY KEY (store_id, tag_id),
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE
);

-- 마커 테이블: 음식점 위치 및 지도 표시용
CREATE TABLE marker (
    marker_id INT NOT NULL AUTO_INCREMENT,
    store_id INT NOT NULL,
    store_name VARCHAR(100) NOT NULL,
    wgs_x DOUBLE NOT NULL,
    wgs_y DOUBLE NOT NULL,
    tm_x DOUBLE NOT NULL,
    tm_y DOUBLE NOT NULL,
    url VARCHAR(500) NOT NULL,
    unicode_name VARCHAR(500) NOT NULL,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    place_id INT NOT NULL,
    PRIMARY KEY (marker_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- 마커-태그 매핑 테이블: 마커에 여러 태그 연결 (다대다)
CREATE TABLE marker_tag_map (
  marker_id INT,
  tag_id INT,
  PRIMARY KEY (marker_id, tag_id),
  FOREIGN KEY (marker_id) REFERENCES marker(marker_id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE
);

-- 마커-카테고리 매핑 테이블
CREATE TABLE marker_category_map (
  marker_id INT,
  category_id INT,
  PRIMARY KEY (marker_id, category_id),
  FOREIGN KEY (marker_id) REFERENCES marker(marker_id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE
);

-- 추천 테이블 (밥풀이 기능)
CREATE TABLE recommend (
    recommend_id INT AUTO_INCREMENT PRIMARY KEY,
    type INT NOT NULL,
    name VARCHAR(50) NOT NULL
);
