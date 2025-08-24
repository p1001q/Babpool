-- 💡 우선 외래키 체크 비활성화 (임시로)
SET FOREIGN_KEY_CHECKS = 0;
-- 삭제 순서: 하위 테이블 → 상위 테이블
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

-- 💡 외래키 체크 비활성화 (임시로)
SET FOREIGN_KEY_CHECKS = 0;
-- 💡 테이블 데이터만 삭제 (테이블 구조는 유지)
TRUNCATE TABLE marker_tag_map;
TRUNCATE TABLE marker_category_map;
TRUNCATE TABLE marker;
TRUNCATE TABLE store_tag_map;
TRUNCATE TABLE store_category_map;
TRUNCATE TABLE bookmark;
TRUNCATE TABLE review_image;
TRUNCATE TABLE review;
TRUNCATE TABLE menu;
TRUNCATE TABLE tag;
TRUNCATE TABLE category;
TRUNCATE TABLE store;
TRUNCATE TABLE user;
-- 외래키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;