-- 0단계: 기존 트리거가 있을 경우 삭제
DROP TRIGGER IF EXISTS `babpooldb`.`bookmark_AFTER_INSERT`;
DROP TRIGGER IF EXISTS `babpooldb`.`bookmark_AFTER_DELETE`;
DROP TRIGGER IF EXISTS `babpooldb`.`review_AFTER_INSERT`;
DROP TRIGGER IF EXISTS `babpooldb`.`review_AFTER_DELETE`;

-- 1단계: 트리거 추가 >>이클립스에서 실행XXX 워크벤치에서 실행해야됨.
DELIMITER $$

USE `babpooldb`$$

-- 트리거: 북마크 추가 시 `like_count` 자동 갱신
CREATE DEFINER = CURRENT_USER TRIGGER `babpooldb`.`bookmark_AFTER_INSERT` 
AFTER INSERT ON `bookmark` 
FOR EACH ROW
BEGIN
    UPDATE store
    SET like_count = like_count + 1
    WHERE store_id = NEW.store_id;
END$$

-- 트리거: 북마크 삭제 시 `like_count` 자동 갱신
CREATE DEFINER = CURRENT_USER TRIGGER `babpooldb`.`bookmark_AFTER_DELETE` 
AFTER DELETE ON `bookmark` 
FOR EACH ROW
BEGIN
    UPDATE store
    SET like_count = like_count - 1
    WHERE store_id = OLD.store_id;
END$$

-- 트리거: 리뷰 작성 시 `rating_avg`와 `review_count` 자동 갱신
CREATE DEFINER = CURRENT_USER TRIGGER `babpooldb`.`review_AFTER_INSERT` 
AFTER INSERT ON `review` 
FOR EACH ROW
BEGIN
    UPDATE store
    SET rating_avg = (SELECT AVG(rating) FROM review WHERE store_id = NEW.store_id),
        review_count = (SELECT COUNT(*) FROM review WHERE store_id = NEW.store_id)
    WHERE store_id = NEW.store_id;
END$$

-- 트리거: 리뷰 삭제 시 `rating_avg`와 `review_count` 자동 갱신
CREATE DEFINER = CURRENT_USER TRIGGER `babpooldb`.`review_AFTER_DELETE` 
AFTER DELETE ON `review` 
FOR EACH ROW
BEGIN
    UPDATE store
    SET rating_avg = (SELECT AVG(rating) FROM review WHERE store_id = OLD.store_id),
        review_count = (SELECT COUNT(*) FROM review WHERE store_id = OLD.store_id)
    WHERE store_id = OLD.store_id;
END$$

DELIMITER ;
