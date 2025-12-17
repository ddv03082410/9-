ALTER TABLE student
    ADD COLUMN speciality_id INT NULL AFTER college_id;

ALTER TABLE course
    ADD COLUMN speciality_id INT NULL AFTER college_id,
    ADD COLUMN is_general TINYINT(1) DEFAULT 0 NOT NULL AFTER speciality_id;

CREATE TABLE IF NOT EXISTS student_completed_course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    completed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scc_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_scc_course FOREIGN KEY (course_id) REFERENCES course(id)
);
