DROP DATABASE IF EXISTS tripdrawdb;
CREATE SCHEMA tripdrawdb;
USE tripdrawdb;

-- 0. user 먼저 생성
CREATE TABLE `user` (
  `user_id`       BIGINT           NOT NULL AUTO_INCREMENT,
  `email`         VARCHAR(100)     NOT NULL,
  `password`      VARCHAR(100)     NOT NULL,
  `name`          VARCHAR(100)     NOT NULL,
  `phone_number`  VARCHAR(100)     DEFAULT NULL,
  `address`       VARCHAR(100)     DEFAULT NULL,
  `addressDetail` VARCHAR(100)     DEFAULT NULL,
  `nickname`      VARCHAR(100)     DEFAULT NULL,
  `login_type`    ENUM('GENERAL','KAKAO','GOOGLE') NOT NULL,
  `role`          VARCHAR(255)     NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_user_email_login_type` (`email`,`login_type`)
);

-- 1. drawing
CREATE TABLE drawing (
  drawing_id          BIGINT       NOT NULL AUTO_INCREMENT,
  user_id             BIGINT       NOT NULL,
  recommend_location  VARCHAR(100) NOT NULL,
  original_image_url  VARCHAR(512) NOT NULL,
  gpt_image_url       VARCHAR(512) NOT NULL,
  created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (drawing_id),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
);


-- 2. free
CREATE TABLE free (
  free_id     BIGINT   NOT NULL AUTO_INCREMENT,
  user_id     BIGINT   NOT NULL,
  title       VARCHAR(255) NOT NULL,
  content     TEXT     NOT NULL,
  view_count  INT      NOT NULL,
  like_count  INT      NOT NULL,
  created_at  DATETIME NOT NULL,
  updated_at  DATETIME NOT NULL,
  PRIMARY KEY (free_id)
);
-- 1) 요약내용(summary) 컬럼 추가
ALTER TABLE free
  ADD COLUMN summary TEXT COMMENT '요약내용'
    AFTER content;

ALTER TABLE free
ADD COLUMN dislike_count INT NOT NULL DEFAULT 0 AFTER like_count;

CREATE TABLE free_reaction (
  reaction_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  free_id       BIGINT NOT NULL,
  user_id       BIGINT NOT NULL,
  reaction_type ENUM('LIKE','DISLIKE') NOT NULL,
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_free_user (free_id, user_id)
);

-- 3. free_comments
CREATE TABLE free_comments (
  comment_id  BIGINT   NOT NULL AUTO_INCREMENT,
  free_id     BIGINT   NOT NULL,
  user_id     BIGINT   NOT NULL,
  content     TEXT     NOT NULL,
  created_at  DATETIME NOT NULL,
  is_deleted  BOOLEAN  NOT NULL,
  PRIMARY KEY (comment_id, free_id),
  FOREIGN KEY (free_id) REFERENCES free(free_id)
);

-- DROP TABLE trip_plans;
-- DROP TABLE trip_board;
-- DROP TABLE trip_location;
-- DROP TABLE trip_comment;

-- 4. trip_plans
CREATE TABLE trip_plans (
  plan_code      VARCHAR(100) NOT NULL COMMENT '계획 공유용 코드',
  user_id        BIGINT       NOT NULL COMMENT '계획 작성자',
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (plan_code),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);

ALTER TABLE trip_plans
  ADD COLUMN region VARCHAR(100) NOT NULL DEFAULT ''
    COMMENT '여행 지역'
    AFTER user_id;

-- 5. trip_location
CREATE TABLE trip_location (
  location_id     BIGINT     NOT NULL AUTO_INCREMENT,
  plan_code       VARCHAR(100) NOT NULL COMMENT '계획 공유용 코드',
  day_no          TINYINT      NOT NULL COMMENT '몇 일차',
  address_name    VARCHAR(100) NOT NULL,
  address_category VARCHAR(100) NOT NULL,
  start_time      TIME         NOT NULL,
  end_time        TIME         NOT NULL,
  PRIMARY KEY (location_id),
  INDEX idx_plan_day (plan_code, day_no),
  FOREIGN KEY (plan_code) REFERENCES trip_plans(plan_code)
);

-- 6. trip_board
CREATE TABLE trip_board (
  plan_board_id   BIGINT       NOT NULL AUTO_INCREMENT,
  plan_code       VARCHAR(100) NOT NULL COMMENT '게시할 여행 계획 코드',
  user_id         BIGINT       NOT NULL COMMENT '게시자(user_id)',
  board_title     VARCHAR(100) NOT NULL,
  board_content   TEXT         NOT NULL,
  board_category  VARCHAR(100) NOT NULL,
  created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (plan_board_id),
  FOREIGN KEY (plan_code) REFERENCES trip_plans(plan_code),
  FOREIGN KEY (user_id)   REFERENCES user(user_id)
);

-- 7. trip_comment
CREATE TABLE trip_comment (
  comment_id    BIGINT   NOT NULL AUTO_INCREMENT,
  plan_board_id BIGINT   NOT NULL,
  user_id       BIGINT   NOT NULL,
  content       TEXT     NOT NULL,
  created_at    DATETIME NOT NULL,
  is_deleted    BOOLEAN  NOT NULL,
  PRIMARY KEY (comment_id, plan_board_id),
  FOREIGN KEY (plan_board_id) REFERENCES trip_board(plan_board_id)
);

-- 8. notice
CREATE TABLE notice (
  notice_id   BIGINT   NOT NULL AUTO_INCREMENT,
  user_id     BIGINT   NOT NULL,
  title       VARCHAR(255) NOT NULL,
  content     TEXT     NOT NULL,
  created_at  DATETIME NOT NULL,
  view_count  INT      NULL,
  is_pinned   BOOLEAN  NULL,
  PRIMARY KEY (notice_id)
);

-- 9. contact
CREATE TABLE contact (
  contact_id  INT      NOT NULL AUTO_INCREMENT,
  user_id     BIGINT   NOT NULL,
  title       VARCHAR(255) NOT NULL,
  content     TEXT     NOT NULL,
  is_secret   BOOLEAN  NOT NULL,
  view_count  INT      NOT NULL,
  created_at  DATETIME NOT NULL,
  PRIMARY KEY (contact_id)
);

-- 10. contact_comment
CREATE TABLE contact_comment (
  comment_id  INT      NOT NULL AUTO_INCREMENT,
  contact_id  INT      NOT NULL,
  user_id     BIGINT   NOT NULL COMMENT '관리자만 작성',
  content     TEXT     NOT NULL,
  created_at  DATETIME NOT NULL,
  PRIMARY KEY (comment_id, contact_id),
  FOREIGN KEY (contact_id) REFERENCES contact(contact_id)
);

-- 11. contact_image
CREATE TABLE contact_image (
  image_id    INT      NOT NULL AUTO_INCREMENT,
  contact_id  INT      NOT NULL,
  image_url   VARCHAR(255) NOT NULL,
  PRIMARY KEY (image_id, contact_id),
  FOREIGN KEY (contact_id) REFERENCES contact(contact_id)
);

-- 12. free_image
CREATE TABLE free_image (
  image_id  BIGINT   NOT NULL AUTO_INCREMENT,
  free_id   BIGINT   NOT NULL,
  image_url VARCHAR(512) NOT NULL,
  PRIMARY KEY (image_id, free_id),
  FOREIGN KEY (free_id) REFERENCES free(free_id)
);

-- 13. 그림 게시판
DROP TABLE IF EXISTS drawing_board;
CREATE TABLE drawing_board (
  drawing_board_id BIGINT       NOT NULL AUTO_INCREMENT,
  drawing_id       BIGINT       NOT NULL,
  user_id          BIGINT       NOT NULL,
  title            VARCHAR(255) NOT NULL,
  content          TEXT         ,
  view_count       INT          NOT NULL DEFAULT 0,
  like_count       INT          NOT NULL DEFAULT 0,
  created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (drawing_board_id),
  FOREIGN KEY (drawing_id) REFERENCES drawing(drawing_id),
  FOREIGN KEY (user_id)    REFERENCES `user`(user_id)
);

-- 16. 그림 게시판 좋아요 위한 테이블
CREATE TABLE user_drawing_like (
  user_id           BIGINT      NOT NULL,
  drawing_board_id  BIGINT      NOT NULL,
  created_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, drawing_board_id),
  FOREIGN KEY (user_id)          REFERENCES `user`(user_id),
  FOREIGN KEY (drawing_board_id) REFERENCES drawing_board(drawing_board_id)
);

CREATE TABLE user_favorite_boards (
  user_id       BIGINT  NOT NULL,
  plan_board_id BIGINT  NOT NULL,
  favorited_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, plan_board_id),
  FOREIGN KEY (user_id)       REFERENCES user(user_id),
  FOREIGN KEY (plan_board_id) REFERENCES trip_board(plan_board_id)
);

-- 1) drawing.user_id → user.user_id
ALTER TABLE drawing
  DROP FOREIGN KEY drawing_ibfk_1,
  ADD CONSTRAINT fk_drawing_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 2) drawing_board.drawing_id → drawing(drawing_id)
--    drawing_board.user_id    → user(user_id)
ALTER TABLE drawing_board
  DROP FOREIGN KEY drawing_board_ibfk_1,
  DROP FOREIGN KEY drawing_board_ibfk_2,
  ADD CONSTRAINT fk_drawing_board_drawing
    FOREIGN KEY (drawing_id)
    REFERENCES drawing(drawing_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT fk_drawing_board_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 3) user_drawing_like.user_id              → user(user_id)
--    user_drawing_like.drawing_board_id     → drawing_board(drawing_board_id)
ALTER TABLE user_drawing_like
  DROP FOREIGN KEY user_drawing_like_ibfk_1, 
  DROP FOREIGN KEY user_drawing_like_ibfk_2, 
  ADD CONSTRAINT fk_udl_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT fk_udl_board
    FOREIGN KEY (drawing_board_id)
    REFERENCES drawing_board(drawing_board_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 4) free_comments.free_id → free(free_id)
ALTER TABLE free_comments
  DROP FOREIGN KEY free_comments_ibfk_1, 
  ADD CONSTRAINT fk_free_comments_free
    FOREIGN KEY (free_id)
    REFERENCES free(free_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 5) trip_plans.user_id → user(user_id)
ALTER TABLE trip_plans
  DROP FOREIGN KEY trip_plans_ibfk_1,  
  ADD CONSTRAINT fk_trip_plans_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 6) trip_location.plan_code → trip_plans(plan_code)
ALTER TABLE trip_location
  DROP FOREIGN KEY trip_location_ibfk_1,  
  ADD CONSTRAINT fk_trip_location_plan
    FOREIGN KEY (plan_code)
    REFERENCES trip_plans(plan_code)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 7) trip_board.plan_code → trip_plans(plan_code)
--    trip_board.user_id   → user(user_id)
ALTER TABLE trip_board
  DROP FOREIGN KEY trip_board_ibfk_1,  
  DROP FOREIGN KEY trip_board_ibfk_2, 
  ADD CONSTRAINT fk_trip_board_plan
    FOREIGN KEY (plan_code)
    REFERENCES trip_plans(plan_code)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT fk_trip_board_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 8) trip_comment.plan_board_id → trip_board(plan_board_id)
ALTER TABLE trip_comment
  DROP FOREIGN KEY trip_comment_ibfk_1, 
  ADD CONSTRAINT fk_trip_comment_board
    FOREIGN KEY (plan_board_id)
    REFERENCES trip_board(plan_board_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 9) contact_comment.contact_id → contact(contact_id)
ALTER TABLE contact_comment
  DROP FOREIGN KEY contact_comment_ibfk_1,  -- ← 실제 이름으로 교체
  ADD CONSTRAINT fk_contact_comment_contact
    FOREIGN KEY (contact_id)
    REFERENCES contact(contact_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 10) contact_image.contact_id → contact(contact_id)
ALTER TABLE contact_image
  DROP FOREIGN KEY contact_image_ibfk_1, 
  ADD CONSTRAINT fk_contact_image_contact
    FOREIGN KEY (contact_id)
    REFERENCES contact(contact_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 11) free_image.free_id → free(free_id)
ALTER TABLE free_image
  DROP FOREIGN KEY free_image_ibfk_1, 
  ADD CONSTRAINT fk_free_image_free
    FOREIGN KEY (free_id)
    REFERENCES free(free_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 12) user_favorite_boards.user_id       → user(user_id)
--     user_favorite_boards.plan_board_id → trip_board(plan_board_id)
ALTER TABLE user_favorite_boards
  DROP FOREIGN KEY user_favorite_boards_ibfk_1,  
  DROP FOREIGN KEY user_favorite_boards_ibfk_2,  
  ADD CONSTRAINT fk_ufb_user
    FOREIGN KEY (user_id)
    REFERENCES `user`(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT fk_ufb_board
    FOREIGN KEY (plan_board_id)
    REFERENCES trip_board(plan_board_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
