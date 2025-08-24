-- 테이블 초기화 (기존 데이터 삭제)
DELETE FROM category;

INSERT INTO category (category_id, name) VALUES
(1, 'korean'),
(2, 'chinese'),
(3, 'western'),
(4, 'japanese'),
(5, 'bunsik'),
(6, 'healthy'),
(7, 'asian'),
(8, 'pub'),
(9, 'cafe');

INSERT INTO tag (tag_id, name) VALUES
(1, 'solo'),
(2, 'group'),
(3, 'delivery'),
(4, 'night');

