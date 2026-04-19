CREATE DATABASE IF NOT EXISTS course_doc_system DEFAULT CHARACTER SET utf8mb4;
USE course_doc_system;

CREATE TABLE IF NOT EXISTS doc_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    course_name VARCHAR(128) NOT NULL,
    doc_type VARCHAR(64),
    author VARCHAR(128),
    file_url VARCHAR(512),
    content LONGTEXT NOT NULL,
    category VARCHAR(64),
    category_confidence DOUBLE,
    keywords VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME,
    FULLTEXT KEY idx_ft_content (title, content, keywords)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
