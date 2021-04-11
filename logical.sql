CREATE TABLE Instrument (
 instrument_id CHAR(10) NOT NULL,
 instrument CHAR(10),
 price INT,
 brand CHAR(10)
);

ALTER TABLE Instrument ADD CONSTRAINT PK_Instrument PRIMARY KEY (instrument_id);


CREATE TABLE Lesson (
 lesson_id CHAR(10) NOT NULL,
 lessonType CHAR(10),
 start_date DATE,
 price INT,
 genre CHAR(10),
 level CHAR(10),
 min_students INT,
 max_students INT
);

ALTER TABLE Lesson ADD CONSTRAINT PK_Lesson PRIMARY KEY (lesson_id);


CREATE TABLE Person (
 ssn INT NOT NULL,
 firstName CHAR(20),
 lastName CHAR(20),
 email CHAR(30),
 birthyear INT,
 phone CHAR(10)
);

ALTER TABLE Person ADD CONSTRAINT PK_Person PRIMARY KEY (ssn);


CREATE TABLE Student (
 student_id CHAR(10) NOT NULL,
 ssn INT NOT NULL,
 sibling_ssn INT,
 skillLevel CHAR(12)
);

ALTER TABLE Student ADD CONSTRAINT PK_Student PRIMARY KEY (student_id);


CREATE TABLE Student_enrollment (
 application_id CHAR(10) NOT NULL,
 sibling_ssn CHAR(10),
 student_id CHAR(10) NOT NULL,
 audition CHAR(10),
 skill_level CHAR(14)
);

ALTER TABLE Student_enrollment ADD CONSTRAINT PK_Student_enrollment PRIMARY KEY (application_id);


CREATE TABLE Adress (
 ssn INT NOT NULL,
 city CHAR(10),
 street CHAR(10),
 zipcode CHAR(10)
);

ALTER TABLE Adress ADD CONSTRAINT PK_Adress PRIMARY KEY (ssn);


CREATE TABLE Instructor (
 instructor_id CHAR(10) NOT NULL,
 ssn INT NOT NULL,
 skill_level CHAR(10)
);

ALTER TABLE Instructor ADD CONSTRAINT PK_Instructor PRIMARY KEY (instructor_id);


CREATE TABLE Payments (
 payment_id CHAR(10) NOT NULL,
 amount CHAR(10),
 date DATE,
 sibling_discount CHAR(10),
 student_id CHAR(10)
);

ALTER TABLE Payments ADD CONSTRAINT PK_Payments PRIMARY KEY (payment_id);


CREATE TABLE Rental_instrument (
 rental_id CHAR(10) NOT NULL,
 instrument_id CHAR(10) NOT NULL,
 rental_date DATE,
 return_date DATE,
 student_id CHAR(10)
);

ALTER TABLE Rental_instrument ADD CONSTRAINT PK_Rental_instrument PRIMARY KEY (rental_id);


CREATE TABLE Salary (
 instructor_id CHAR(10) NOT NULL,
 amount CHAR(10),
 payment_date DATE
);

ALTER TABLE Salary ADD CONSTRAINT PK_Salary PRIMARY KEY (instructor_id);


CREATE TABLE Student_lesson_instructor (
 sli_id CHAR(10) NOT NULL,
 lesson_id CHAR(10) NOT NULL,
 instructor_id CHAR(10) NOT NULL
);

ALTER TABLE Student_lesson_instructor ADD CONSTRAINT PK_Student_lesson_instructor PRIMARY KEY (sli_id);


CREATE TABLE Attendance_list (
 attendance_id CHAR(10) NOT NULL,
 student_id CHAR(10) NOT NULL,
 sli_id CHAR(10)
);

ALTER TABLE Attendance_list ADD CONSTRAINT PK_Attendance_list PRIMARY KEY (attendance_id);


ALTER TABLE Student ADD CONSTRAINT FK_Student_0 FOREIGN KEY (ssn) REFERENCES Person (ssn);


ALTER TABLE Student_enrollment ADD CONSTRAINT FK_Student_enrollment_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE Adress ADD CONSTRAINT FK_Adress_0 FOREIGN KEY (ssn) REFERENCES Person (ssn);


ALTER TABLE Instructor ADD CONSTRAINT FK_Instructor_0 FOREIGN KEY (ssn) REFERENCES Person (ssn);


ALTER TABLE Payments ADD CONSTRAINT FK_Payments_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE Rental_instrument ADD CONSTRAINT FK_Rental_instrument_0 FOREIGN KEY (instrument_id) REFERENCES Instrument (instrument_id);
ALTER TABLE Rental_instrument ADD CONSTRAINT FK_Rental_instrument_1 FOREIGN KEY (student_id) REFERENCES Student (student_id);


ALTER TABLE Salary ADD CONSTRAINT FK_Salary_0 FOREIGN KEY (instructor_id) REFERENCES Instructor (instructor_id);


ALTER TABLE Student_lesson_instructor ADD CONSTRAINT FK_Student_lesson_instructor_0 FOREIGN KEY (lesson_id) REFERENCES Lesson (lesson_id);
ALTER TABLE Student_lesson_instructor ADD CONSTRAINT FK_Student_lesson_instructor_1 FOREIGN KEY (instructor_id) REFERENCES Instructor (instructor_id);


ALTER TABLE Attendance_list ADD CONSTRAINT FK_Attendance_list_0 FOREIGN KEY (student_id) REFERENCES Student (student_id);
ALTER TABLE Attendance_list ADD CONSTRAINT FK_Attendance_list_1 FOREIGN KEY (sli_id) REFERENCES Student_lesson_instructor (sli_id);


