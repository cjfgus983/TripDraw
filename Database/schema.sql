DROP DATABASE IF EXISTS tripdrawdb;
CREATE SCHEMA tripdrawdb;
USE tripdrawdb;

-- 1. drawing
CREATE TABLE drawing (
  drawing_id       BIGINT       NOT NULL AUTO_INCREMENT,
  image_type       ENUM('original','gpt') NOT NULL,
  user_id          BIGINT       NOT NULL,
  recommend_location VARCHAR(100) NOT NULL,
  created_at       DATETIME     NOT NULL,
  image_url        VARCHAR(255) NOT NULL,
  PRIMARY KEY (drawing_id, image_type)
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

-- 4. trip_plans
CREATE TABLE trip_plans (
  plan_code        VARCHAR(100) NOT NULL COMMENT '계획 공유용 코드',
  user_id          BIGINT       NOT NULL,
  trip_category    VARCHAR(100) NOT NULL,
  plan_title       VARCHAR(100) NOT NULL,
  plan_description VARCHAR(100) NOT NULL,
  PRIMARY KEY (plan_code)
);

-- 5. trip_location
CREATE TABLE trip_location (
  location_id   BIGINT       NOT NULL AUTO_INCREMENT,
  plan_code     VARCHAR(100) NOT NULL COMMENT '계획 공유용 코드',
  address_name  VARCHAR(100) NOT NULL,
  address       VARCHAR(100) NOT NULL,
  start_time    TIME         NOT NULL COMMENT '정렬 기준',
  end_time      TIME         NOT NULL,
  PRIMARY KEY (location_id, plan_code),
  FOREIGN KEY (plan_code) REFERENCES trip_plans(plan_code)
);

-- 6. trip_board
CREATE TABLE trip_board (
  plan_board_id BIGINT       NOT NULL AUTO_INCREMENT,
  plan_code     VARCHAR(100) NOT NULL COMMENT '계획 공유용 코드',
  PRIMARY KEY (plan_board_id)
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

-- 13. drawing_board
CREATE TABLE drawing_board (
  drawing_board_id BIGINT     NOT NULL AUTO_INCREMENT,
  drawing_id       BIGINT     NOT NULL,
  image_type       ENUM('original','gpt') NOT NULL,
  user_id          BIGINT     NOT NULL,
  title            VARCHAR(255) NOT NULL,
  content          TEXT       NOT NULL,
  view_count       INT        NOT NULL,
  like_count       INT        NOT NULL,
  created_at       DATETIME   NOT NULL,
  updated_at       DATETIME   NOT NULL,
  PRIMARY KEY (drawing_board_id, drawing_id, image_type),
  FOREIGN KEY (drawing_id, image_type) REFERENCES drawing(drawing_id, image_type)
);

-- 14. user
CREATE TABLE `user` (
  `user_id`       BIGINT           NOT NULL AUTO_INCREMENT,
  `email`         VARCHAR(100)     NOT NULL,
  `password`      VARCHAR(100)     NOT NULL,
  `name`          VARCHAR(100)     NOT NULL,
  `phone_number`  VARCHAR(100)     DEFAULT NULL,
  `address`       VARCHAR(100)     DEFAULT NULL,
  `addressDetail`      VARCHAR(100)     DEFAULT NULL,
  `nickname`      VARCHAR(100)     DEFAULT NULL,
  `login_type`    ENUM('GENERAL','KAKAO','GOOGLE')
                    COMMENT '로그인 유형',
  `role`          VARCHAR(255)     NOT NULL DEFAULT 'USER'
                    COMMENT '사용자 권한',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_user_email_login_type` (`email`,`login_type`)
)


