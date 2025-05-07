drop database if exists tripdrawdb;
create schema tripdrawdb;
use tripdrawdb;

CREATE TABLE `drawing` (
	`drawing_id`	BIGINT	NOT NULL,
	`image_type`	ENUM('original', 'gpt')	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`recommend_location`	VARCHAR(100)	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`image_url`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `free_comments` (
	`comment_id`	BIGINT	NOT NULL,
	`free_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`content`	TEXT	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`is_deleted`	BOOLEAN	NOT NULL
);

CREATE TABLE `trip_plans` (
	`plan_code`	VARCHAR(100)	NOT NULL	COMMENT '계획 공유를 위한 칼럼',
	`user_id`	BIGINT	NOT NULL,
	`trip_category`	VARCHAR(100)	NOT NULL,
	`plan_title`	VARCHAR(100)	NOT NULL,
	`plan_description`	VARCHAR(100)	NOT NULL
);

CREATE TABLE `free` (
	`free_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	TEXT	NOT NULL,
	`view_count`	INT	NOT NULL,
	`like_count`	INT	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NOT NULL
);

CREATE TABLE `notice` (
	`notice_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	TEXT	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`view_count`	INT	NULL,
	`is_pinned`	BOOLEAN	NULL
);

CREATE TABLE `contact` (
	`contact_id`	INT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	TEXT	NOT NULL,
	`is_secret`	BOOLEAN	NOT NULL,
	`view_count`	INT	NOT NULL,
	`created_at`	DATETIME	NOT NULL
);

CREATE TABLE `contact_comment` (
	`comment_id`	INT	NOT NULL,
	`contact_id`	INT	NOT NULL,
	`user_id`	BIGINT	NOT NULL	COMMENT 'admin만 쓸 수 있음',
	`content`	TEXT	NOT NULL,
	`created_at`	DATETIME	NOT NULL
);

CREATE TABLE `drawing_board` (
	`drawing_board_id`	BIGINT	NOT NULL,
	`drawing_id`	BIGINT	NOT NULL,
	`image_type`	ENUM('original', 'gpt')	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	TEXT	NOT NULL,
	`view_count`	INT	NOT NULL,
	`like_count`	INT	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NOT NULL
);

CREATE TABLE `user` (
	`user_id`	BIGINT	NOT NULL,
	`email`	VARCHAR(100)	NOT NULL,
	`password`	VARCHAR(100)	NOT NULL,
	`name`	VARCHAR(100)	NOT NULL,
	`phone_number`	VARCHAR(100)	NULL,
	`address`	VARCHAR(100)	NULL,
	`is_kakao_login`	BOOLEAN	NOT NULL,
	`role`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `trip_comment` (
	`comment_id`	BIGINT	NOT NULL,
	`plan_board_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
	`content`	TEXT	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`is_deleted`	BOOLEAN	NOT NULL
);

CREATE TABLE `contact_image` (
	`image_id`	INT	NOT NULL,
	`contact_id`	INT	NOT NULL,
	`image_url`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `trip_location` (
	`location_id`	BIGINT	NOT NULL,
	`plan_code`	VARCHAR(100)	NOT NULL	COMMENT '계획 공유를 위한 칼럼',
	`address_name`	VARCHAR(100)	NOT NULL,
	`address`	VARCHAR(100)	NOT NULL,
	`start_time`	TIME	NOT NULL	COMMENT '정렬할 기준',
	`end_time`	TIME	NOT NULL
);

CREATE TABLE `free_image` (
	`image_id`	BIGINT	NOT NULL,
	`free_id`	BIGINT	NOT NULL,
	`image_url`	VARCHAR(512)	NOT NULL
);

CREATE TABLE `trip_board` (
	`plan_board_id`	BIGINT	NOT NULL,
	`plan_code`	VARCHAR(100)	NOT NULL	COMMENT '계획 공유를 위한 칼럼'
);

ALTER TABLE `drawing` ADD CONSTRAINT `PK_DRAWING` PRIMARY KEY (
	`drawing_id`,
	`image_type`
);

ALTER TABLE `free_comments` ADD CONSTRAINT `PK_FREE_COMMENTS` PRIMARY KEY (
	`comment_id`,
	`free_id`
);

ALTER TABLE `trip_plans` ADD CONSTRAINT `PK_TRIP_PLANS` PRIMARY KEY (
	`plan_code`
);

ALTER TABLE `free` ADD CONSTRAINT `PK_FREE` PRIMARY KEY (
	`free_id`
);

ALTER TABLE `notice` ADD CONSTRAINT `PK_NOTICE` PRIMARY KEY (
	`notice_id`
);

ALTER TABLE `contact` ADD CONSTRAINT `PK_CONTACT` PRIMARY KEY (
	`contact_id`
);

ALTER TABLE `contact_comment` ADD CONSTRAINT `PK_CONTACT_COMMENT` PRIMARY KEY (
	`comment_id`,
	`contact_id`
);

ALTER TABLE `drawing_board` ADD CONSTRAINT `PK_DRAWING_BOARD` PRIMARY KEY (
	`drawing_board_id`,
	`drawing_id`,
	`image_type`
);

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `trip_comment` ADD CONSTRAINT `PK_TRIP_COMMENT` PRIMARY KEY (
	`comment_id`,
	`plan_board_id`
);

ALTER TABLE `contact_image` ADD CONSTRAINT `PK_CONTACT_IMAGE` PRIMARY KEY (
	`image_id`,
	`contact_id`
);

ALTER TABLE `trip_location` ADD CONSTRAINT `PK_TRIP_LOCATION` PRIMARY KEY (
	`location_id`,
	`plan_code`
);

ALTER TABLE `free_image` ADD CONSTRAINT `PK_FREE_IMAGE` PRIMARY KEY (
	`image_id`,
	`free_id`
);

ALTER TABLE `trip_board` ADD CONSTRAINT `PK_TRIP_BOARD` PRIMARY KEY (
	`plan_board_id`
);

ALTER TABLE `free_comments` ADD CONSTRAINT `FK_free_TO_free_comments_1` FOREIGN KEY (
	`free_id`
)
REFERENCES `free` (
	`free_id`
);

ALTER TABLE `contact_comment` ADD CONSTRAINT `FK_contact_TO_contact_comment_1` FOREIGN KEY (
	`contact_id`
)
REFERENCES `contact` (
	`contact_id`
);


ALTER TABLE `drawing_board`
ADD CONSTRAINT FK_drawing_board_to_drawing
FOREIGN KEY (`drawing_id`, `image_type`)
REFERENCES `drawing` (`drawing_id`, `image_type`);

ALTER TABLE `trip_comment` ADD CONSTRAINT `FK_trip_board_TO_trip_comment_1` FOREIGN KEY (
	`plan_board_id`
)
REFERENCES `trip_board` (
	`plan_board_id`
);

ALTER TABLE `contact_image` ADD CONSTRAINT `FK_contact_TO_contact_image_1` FOREIGN KEY (
	`contact_id`
)
REFERENCES `contact` (
	`contact_id`
);

ALTER TABLE `trip_location` ADD CONSTRAINT `FK_trip_plans_TO_trip_location_1` FOREIGN KEY (
	`plan_code`
)
REFERENCES `trip_plans` (
	`plan_code`
);

ALTER TABLE `free_image` ADD CONSTRAINT `FK_free_TO_free_image_1` FOREIGN KEY (
	`free_id`
)
REFERENCES `free` (
	`free_id`
);

