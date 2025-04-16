-- Teacher Table
CREATE TABLE teacher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Category Table
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Quiz Table
CREATE TABLE quiz (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    course_code VARCHAR(50),
    published BOOLEAN DEFAULT FALSE,
    teacher_id BIGINT,
    category_id BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Question Table
CREATE TABLE question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    difficulty_level ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    quiz_id BIGINT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
);

-- Choice Table
CREATE TABLE choice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    question_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- Student Table
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Review Table
CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    student_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

-- Feedback Table
CREATE TABLE feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message TEXT,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    question_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- Student Quiz attempts (join table for students taking quizzes)
CREATE TABLE student_quiz (
    student_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score FLOAT,
    PRIMARY KEY (student_id, quiz_id, attempt_date),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

