package integration.init;

import java.sql.Connection;
import java.sql.SQLException;

public class TestInitialization {
    public static void createSchema(Connection connection) throws SQLException {
        connection.createStatement().execute("""
            CREATE TABLE users (
                id INT AUTO_INCREMENT NOT NULL UNIQUE,
                username VARCHAR(255) PRIMARY KEY,
                email VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL
            );
            CREATE TABLE degree_program (
                degree_program_id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                code VARCHAR(50) NOT NULL,
                department VARCHAR(255)
            );
            CREATE TABLE professor (
                professor_id VARCHAR(16) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                department VARCHAR(255),
                user_id INT,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
            CREATE TABLE student (
                student_id VARCHAR(16) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                degree_program_id INT NOT NULL,
                year_of_study INT,
                user_id INT,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (degree_program_id) REFERENCES degree_program(degree_program_id)
            );
            CREATE TABLE course (
                course_id VARCHAR(16) NOT NULL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                credits INT NOT NULL,
                degree_program_id INT NOT NULL,
                professor_id VARCHAR(16) NOT NULL,
                FOREIGN KEY (degree_program_id) REFERENCES degree_program(degree_program_id),
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
                grade VARCHAR(20),
                status VARCHAR(20),
                PRIMARY KEY (exam_session_id, student_id),
                FOREIGN KEY (exam_session_id) REFERENCES exam_session(exam_session_id),
                FOREIGN KEY (student_id) REFERENCES student(student_id)
            );
            CREATE TABLE exam_verbal(
                exam_verbal_id INT AUTO_INCREMENT,
                exam_session_id INT NOT NULL,
                creation_date DATE NOT NULL,
                PRIMARY KEY(exam_verbal_id),
                FOREIGN KEY (exam_session_id) REFERENCES exam_session(exam_session_id)
            );
        """);
    }

    public static void insertTestUsers(Connection connection) throws SQLException {
        connection.createStatement().execute("""
            INSERT INTO users(username,email, password, role) VALUES
            ('studente@test.com','studente@test.com', '1234', 'STUDENT'),
            ('prof@test.com','prof@test.com', 'abcd', 'TEACHER');

            INSERT INTO degree_program(name, code, department) VALUES
            ('Computer Science BSc', 'L-31', 'Department of Computer Science'),
            ('Mathematics BSc', 'L-35', 'Department of Mathematics');
            
            INSERT INTO professor(professor_id, first_name, last_name, email, department) VALUES
            ('prof01', 'John', 'Doe', 'john.doe@university.com', 'Computer Science'),
            ('prof02', 'Alice', 'Smith', 'alice.smith@university.com', 'Mathematics');
            
            INSERT INTO student(student_id, first_name, last_name, email, degree_program_id, year_of_study) VALUES
            ('stud01', 'Mark', 'Johnson', 'mark.johnson@student.com', 1, 2),
            ('stud02', 'Emily', 'Brown', 'emily.brown@student.com', 1, 1),
            ('stud03', 'David', 'White', 'david.white@student.com', 2, 1);
            
            INSERT INTO course(course_id, name, credits, degree_program_id, professor_id) VALUES
            ('CS101','Object Oriented Programming',  12, 1, 'prof01'),
            ( 'CS201','Algorithms and Data Structures', 9, 1, 'prof01'),
            ( 'MA101', 'Linear Algebra',6, 2, 'prof02');
            
            INSERT INTO exam_session(course_id, session_date, room, type) VALUES
            ('CS101', DATE '2025-02-10', 'A1', 'Written'),
            ('CS101', DATE '2025-07-15', 'A2', 'Oral'),
            ('CS101', DATE '2025-07-17', 'A3', 'Written'),
            ('CS201', DATE '2025-03-20', 'B1', 'Written'),
            ('MA101', DATE '2025-02-25', 'C1', 'Written');
            
            INSERT INTO exam_enrollment(exam_session_id, student_id, enrollment_date) VALUES
            (1, 'stud01', DATE '2025-01-20'),
            (1, 'stud02', DATE '2025-01-21'),
            (1, 'stud03', DATE '2025-01-21'),
            (3, 'stud01', DATE '2025-03-01'),
            (4, 'stud03', DATE '2025-02-10');
            
            INSERT INTO exam_result(exam_session_id, student_id, grade, status) VALUES
            (1, 'stud01', '<vuoto>', 'non inserito'),
            (1, 'stud02', '<vuoto>', 'non inserito'),
            (4, 'stud03',  '<vuoto>', 'non inserito');
        """);
    }

}
