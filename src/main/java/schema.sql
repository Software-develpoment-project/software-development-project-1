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

-- Student Answer Table (tracks each answer in an attempt)
CREATE TABLE student_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    attempt_id BIGINT NOT NULL, -- This should map to the ID of student_quiz if it gets an AUTO_INCREMENT PK
    question_id BIGINT NOT NULL,
    chosen_answer_id BIGINT NOT NULL, -- FK to answer_option table (assuming it's named answer_option, not choice)
    is_correct BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- If student_quiz uses composite key and no auto_increment id, attempt_id might need to be part of a composite FK or design adjusted.
    -- For simplicity with QuizAttempt having its own ID:
    -- FOREIGN KEY (attempt_id) REFERENCES student_quiz(id), -- This assumes student_quiz gets an ID PK
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE, -- Or SET NULL depending on policy
    FOREIGN KEY (chosen_answer_id) REFERENCES choice(id) -- Use the correct table name
    -- If student_quiz table keeps composite PK (student_id, quiz_id, attempt_date)
    -- then linking attempt_id here is more complex. The QuizAttempt entity has its own Long id.
    -- A simple FK to QuizAttempt.id is preferred if student_quiz is altered or if QuizAttempt.id is a conceptual attempt ID.
);

-- Review Table  
CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    student_nickname VARCHAR(100) NOT NULL, -- 'nickname' field as per UI
    rating INT NOT NULL, -- Numeric rating 1-5
    review_text TEXT,    -- 'review' field as per UI (text)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
);

