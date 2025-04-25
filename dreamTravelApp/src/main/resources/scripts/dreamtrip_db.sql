CREATE DATABASE IF NOT EXISTS `dreamtripdb`;
USE `dreamtripdb`;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender ENUM('M', 'F') DEFAULT 'M',
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO user (name, email, password, gender, birth_date)
VALUES 
('홍길동', 'hong@example.com', 'password123', 'M', '1995-01-01'),
('김영희', 'kim@example.com', '1234abcd', 'F', '1998-03-25'),
('이철수', 'lee@example.com', 'qwerty123', 'M', '1990-07-10'),
('박민수', 'park@example.com', 'hello2024', 'M', '1993-12-31');
