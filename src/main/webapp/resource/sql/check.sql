SHOW TABLES;

COMMIT;

-- ✅ 각 테이블의 현재 데이터 확인 (초기에는 빈 테이블일 수도 있음)
SELECT * FROM user;
SELECT * FROM store;
SELECT * FROM category;
SELECT * FROM tag;
SELECT * FROM menu;
SELECT * FROM review;
SELECT * FROM review_image;
SELECT * FROM bookmark;
SELECT * FROM store_category_map;
SELECT * FROM store_tag_map;
SELECT * FROM marker;
SELECT * FROM marker_tag_map;
SELECT * FROM marker_category_map;

--------------------------------------------------
SHOW CREATE TABLE user;
SHOW CREATE TABLE store;
SHOW CREATE TABLE category;
SHOW CREATE TABLE tag;
SHOW CREATE TABLE menu;
SHOW CREATE TABLE review;
SHOW CREATE TABLE review_image;
SHOW CREATE TABLE bookmark;
SHOW CREATE TABLE store_category_map;
SHOW CREATE TABLE store_tag_map;
SHOW CREATE TABLE recommendation_log;
SHOW CREATE TABLE user_language;
SHOW CREATE TABLE marker;
SHOW CREATE TABLE marker_tag_map;