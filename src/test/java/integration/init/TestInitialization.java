package integration.init;

import java.sql.Connection;
import java.sql.SQLException;

public class TestInitialization {
    public static void createSchema(Connection connection) throws SQLException {
        connection.createStatement().execute("""
            CREATE TABLE users (
                username VARCHAR(255) PRIMARY KEY,
                email VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL
            );
            CREATE TABLE degree_program (
                name VARCHAR(255) NOT NULL,
                code VARCHAR(50) NOT NULL PRIMARY KEY,
                department VARCHAR(255)
            );
            CREATE TABLE professor (
                professor_id VARCHAR(16) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                department VARCHAR(255),
                username VARCHAR(255),
                FOREIGN KEY (username) REFERENCES users(username)
            );
            CREATE TABLE student (
                student_id VARCHAR(16) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                degree_program_code VARCHAR(50) NOT NULL,
                year_of_study INT,
                username VARCHAR(255),
                FOREIGN KEY (username) REFERENCES users(username),
                FOREIGN KEY (degree_program_code) REFERENCES degree_program(code)
            );
            CREATE TABLE course (
                course_id VARCHAR(16) NOT NULL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                credits INT NOT NULL,
                degree_program_code VARCHAR(50) NOT NULL,
                professor_id VARCHAR(16) NOT NULL,
                FOREIGN KEY (degree_program_code) REFERENCES degree_program(code),
                FOREIGN KEY (professor_id) REFERENCES professor(professor_id)
            );
            CREATE TABLE exam_session (
                exam_session_id INT AUTO_INCREMENT PRIMARY KEY,
                course_id VARCHAR(16) NOT NULL,
                session_date DATE NOT NULL,
                room VARCHAR(50),
                type VARCHAR(50),
                FOREIGN KEY (course_id) REFERENCES course(course_id)
            );
            CREATE TABLE exam_enrollment (
                exam_session_id INT NOT NULL,
                student_id VARCHAR(16) NOT NULL,
                enrollment_date DATE NOT NULL,
                PRIMARY KEY (exam_session_id, student_id),
                FOREIGN KEY (exam_session_id) REFERENCES exam_session(exam_session_id),
                FOREIGN KEY (student_id) REFERENCES student(student_id)
            );
            CREATE TABLE exam_result (
                exam_session_id INT NOT NULL,
                student_id VARCHAR(16) NOT NULL,
                grade VARCHAR(16),
                status VARCHAR(16),
                PRIMARY KEY (exam_session_id, student_id),
                FOREIGN KEY (exam_session_id) REFERENCES exam_session(exam_session_id),
                FOREIGN KEY (student_id) REFERENCES student(student_id)
            );
            CREATE TABLE exam_verbal(
                exam_verbal_id VARCHAR(255),
                exam_session_id INT NOT NULL,
                creation_timestamp DATETIME NOT NULL,
                professor_id VARCHAR(16) NOT NULL,
                PRIMARY KEY(exam_verbal_id),
                FOREIGN KEY (exam_session_id) REFERENCES exam_session(exam_session_id),
                FOREIGN KEY (professor_id) REFERENCES professor(professor_id)
            );
        """);
    }

    public static void insertTestUsers(Connection connection) throws SQLException {
        connection.createStatement().execute("""
            INSERT INTO users(username, email, password, role) VALUES
              ('stud01@test.com','stud01@test.com','1111','STUDENT'),
              ('stud02@test.com','stud02@test.com','2222','STUDENT'),
              ('stud03@test.com','stud03@test.com','3333','STUDENT'),
              ('stud04@test.com','stud04@test.com','4444','STUDENT'),
              ('prof01@test.com','prof01@test.com','aaaa','TEACHER'),
              ('prof02@test.com','prof02@test.com','bbbb','TEACHER'),
              ('prof03@test.com','prof03@test.com','cccc','TEACHER'),
              ('admin@test.com','admin@test.com','root','TEACHER');
              
              INSERT INTO degree_program(name, code, department) VALUES
              ('Computer Science BSc', 'L-31', 'Department of Computer Science'),
              ('Mathematics BSc', 'L-35', 'Department of Mathematics'),
              ('Physics BSc', 'L-30', 'Department of Physics');
              
              INSERT INTO professor(professor_id, first_name, last_name, email, department, username) VALUES
              ('prof01', 'John', 'Doe', 'john.doe@university.com', 'Department of Computer Science', 'prof01@test.com'),
              ('prof02', 'Alice', 'Smith', 'alice.smith@university.com', 'Department of Mathematics', 'prof02@test.com'),
              ('prof03', 'Robert', 'King', 'robert.king@university.com', 'Department of Physics','prof03@test.com');
              
              INSERT INTO student(student_id, first_name, last_name, email, degree_program_code, year_of_study, username) VALUES
              ('stud01', 'Mark', 'Johnson', 'mark.johnson@student.com', 'L-31', 2, 'stud01@test.com'),
              ('stud02', 'Emily', 'Brown', 'emily.brown@student.com', 'L-31', 1, 'stud02@test.com'),
              ('stud03', 'David', 'White', 'david.white@student.com', 'L-35', 1, 'stud03@test.com'),
              ('stud04', 'Laura', 'Green', 'laura.green@student.com', 'L-30', 3, 'stud04@test.com');
              
              INSERT INTO course(course_id, name, credits, degree_program_code, professor_id) VALUES
              ('CS101','Object Oriented Programming', 12, 'L-31', 'prof01'),
              ('CS201','Algorithms and Data Structures', 9, 'L-31', 'prof01'),
              ('CS301','Operating Systems', 9, 'L-31', 'prof01'),
              ('MA101','Linear Algebra', 6, 'L-35', 'prof02'),
              ('MA201','Calculus II', 9, 'L-35', 'prof02'),
              ('PH101','Classical Mechanics', 6, 'L-30', 'prof03');
              
              INSERT INTO exam_session(course_id, session_date, room, type) VALUES
              ('CS101', DATE '2025-02-10', 'A1', 'Written'),
              ('CS101', DATE '2025-07-15', 'A2', 'Oral'),
              ('CS101', DATE '2025-07-17', 'A3', 'Written'),
              ('CS201', DATE '2025-03-20', 'B1', 'Written'),
              ('CS301', DATE '2025-05-10', 'B2', 'Written'),
              ('MA101', DATE '2025-02-25', 'C1', 'Written'),
              ('MA201', DATE '2025-06-03', 'C2', 'Oral'),
              ('PH101', DATE '2025-04-12', 'D1', 'Written');
              
              INSERT INTO exam_enrollment(exam_session_id, student_id, enrollment_date) VALUES
              (1, 'stud01', DATE '2025-01-20'),
              (1, 'stud02', DATE '2025-01-21'),
              (1, 'stud03', DATE '2025-01-21'),
              (2, 'stud01', DATE '2025-06-20'),
              (3, 'stud02', DATE '2025-06-22'),
              (4, 'stud03', DATE '2025-02-01'),
              (5, 'stud01', DATE '2025-04-20'),
              (6, 'stud03', DATE '2025-02-10'),
              (6, 'stud01', DATE '2025-02-11'),
              (7, 'stud02', DATE '2025-05-01'),
              (8, 'stud04', DATE '2025-03-15');
              
              INSERT INTO exam_result(exam_session_id, student_id, grade, status) VALUES
              (1, 'stud01', '<vuoto>', 'non inserito'),
              (1, 'stud02', '<vuoto>', 'non inserito'),
              (1, 'stud03', '18', 'inserito'),
              (2, 'stud01', '<vuoto>', 'non inserito'),
              (2, 'stud02', '<vuoto>', 'non inserito'),
              (2, 'stud03', '<vuoto>', 'non inserito'),
              (2, 'stud04', '18', 'pubblicato'),
              (3, 'stud02', '<vuoto>', 'non inserito'),
              (4, 'stud03', '<vuoto>', 'non inserito'),
              (5, 'stud01', '24', 'pubblicato'),
              (6, 'stud03', '18', 'inserito'),
              (6, 'stud01', '18', 'inserito'),
              (6, 'stud02', '<vuoto>', 'non inserito'),
              (7, 'stud02', '<vuoto>', 'non inserito'),
              (8, 'stud04', '<vuoto>', 'non inserito'); 
        """);
    }
}
