CREATE TABLE Author (
                        Author_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        Name VARCHAR(100) NOT NULL,
                        Country VARCHAR(50)
);

CREATE TABLE Publisher (
                           Publisher_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           Name VARCHAR(150) NOT NULL,
                           Address VARCHAR(255)
);

CREATE TABLE Category (
                          Category_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          Name VARCHAR(100) NOT NULL
);

CREATE TABLE Book (
                      Book_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      Title VARCHAR(200) NOT NULL,
                      ISBN VARCHAR(20) UNIQUE,
                      Publish_Year INT,
                      Page_Count INT,

                      Author_Id INT,
                      Publisher_Id INT,
                      Category_Id INT,

                      FOREIGN KEY (Author_Id) REFERENCES Author(Author_Id),
                      FOREIGN KEY (Publisher_Id) REFERENCES Publisher(Publisher_Id),
                      FOREIGN KEY (Category_Id) REFERENCES Category(Category_Id)
);

CREATE TABLE Book_Copy (
                           Copy_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           Book_Id INT NOT NULL,
                           Status VARCHAR(30) DEFAULT 'Available',
                           Shelf_Location VARCHAR(50),

                           FOREIGN KEY (Book_Id) REFERENCES Book(Book_Id)
);

CREATE TABLE Student (
                         Student_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         Name VARCHAR(100) NOT NULL,
                         Email VARCHAR(100) UNIQUE,
                         Phone VARCHAR(20),
                         Registered_Date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE Staff (
                       Staff_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       Name VARCHAR(100) NOT NULL,
                       Role VARCHAR(50),
                       Hire_Date DATE
);

CREATE TABLE Loan (
                      Loan_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      Copy_Id INT NOT NULL,
                      Student_Id INT NOT NULL,
                      Staff_Id INT,

                      Loan_Date DATE NOT NULL,
                      Due_Date DATE NOT NULL,

                      FOREIGN KEY (Copy_Id) REFERENCES Book_Copy(Copy_Id),
                      FOREIGN KEY (Student_Id) REFERENCES Student(Student_Id),
                      FOREIGN KEY (Staff_Id) REFERENCES Staff(Staff_Id)
);

CREATE TABLE Return_Table (
                              Return_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              Loan_Id INT NOT NULL,
                              Return_Date DATE NOT NULL,
                              Condition_Notes VARCHAR(255),

                              FOREIGN KEY (Loan_Id) REFERENCES Loan(Loan_Id)
);

CREATE TABLE Fine (
                      Fine_Id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      Loan_Id INT NOT NULL,
                      Amount NUMERIC(10,2) NOT NULL,
                      Paid BOOLEAN DEFAULT FALSE,
                      Issue_Date DATE DEFAULT CURRENT_DATE,

                      FOREIGN KEY (Loan_Id) REFERENCES Loan(Loan_Id)
);

INSERT INTO Author (Name, Country) VALUES
                                       ('J.K. Rowling', 'UK'),
                                       ('George Orwell', 'UK'),
                                       ('Orhan Pamuk', 'Turkey'),
                                       ('Dan Brown', 'USA'),
                                       ('Fyodor Dostoevsky', 'Russia');

INSERT INTO Publisher (Name, Address) VALUES
                                          ('Penguin Books', 'London'),
                                          ('HarperCollins', 'New York'),
                                          ('Can Yayınları', 'Istanbul'),
                                          ('Random House', 'Berlin'),
                                          ('Oxford Press', 'Oxford');

INSERT INTO Category (Name) VALUES
                                ('Fantasy'),
                                ('Dystopian'),
                                ('Novel'),
                                ('Thriller'),
                                ('Classic');

INSERT INTO Book (Title, ISBN, Publish_Year, Page_Count, Author_Id, Publisher_Id, Category_Id) VALUES
                                                                                                   ('Harry Potter', '1111', 1997, 320, 1, 1, 1),
                                                                                                   ('1984', '2222', 1949, 250, 2, 2, 2),
                                                                                                   ('Kar', '3333', 2002, 300, 3, 3, 3),
                                                                                                   ('Da Vinci Code', '4444', 2003, 400, 4, 4, 4),
                                                                                                   ('Crime and Punishment', '5555', 1866, 500, 5, 5, 5);

INSERT INTO Book_Copy (Book_Id, Status, Shelf_Location) VALUES
                                                            (1, 'Available', 'A1'),
                                                            (2, 'Borrowed', 'B2'),
                                                            (3, 'Available', 'C3'),
                                                            (4, 'Available', 'D4'),
                                                            (5, 'Borrowed', 'E5');

INSERT INTO Student (Name, Email, Phone) VALUES
                                             ('Ali Yılmaz', 'ali@mail.com', '111111'),
                                             ('Ayşe Kaya', 'ayse@mail.com', '222222'),
                                             ('Mehmet Demir', 'mehmet@mail.com', '333333'),
                                             ('Zeynep Aydın', 'zeynep@mail.com', '444444'),
                                             ('Can Çelik', 'can@mail.com', '555555');

INSERT INTO Staff (Name, Role, Hire_Date) VALUES
                                              ('Ahmet', 'Librarian', '2020-01-10'),
                                              ('Fatma', 'Assistant', '2021-03-15'),
                                              ('Hasan', 'Manager', '2019-06-20'),
                                              ('Elif', 'Librarian', '2022-02-11'),
                                              ('Murat', 'Assistant', '2023-05-05');

INSERT INTO Loan (Copy_Id, Student_Id, Staff_Id, Loan_Date, Due_Date) VALUES
                                                                          (1, 1, 1, '2024-01-01', '2024-01-10'),
                                                                          (2, 2, 2, '2024-01-02', '2024-01-12'),
                                                                          (3, 3, 3, '2024-01-03', '2024-01-13'),
                                                                          (4, 4, 4, '2024-01-04', '2024-01-14'),
                                                                          (5, 5, 5, '2024-01-05', '2024-01-15');

INSERT INTO Return_Table (Loan_Id, Return_Date, Condition_Notes) VALUES
                                                                     (1, '2024-01-09', 'Good'),
                                                                     (2, '2024-01-15', 'Late'),
                                                                     (3, '2024-01-12', 'Good'),
                                                                     (4, '2024-01-20', 'Damaged'),
                                                                     (5, '2024-01-14', 'Good');

INSERT INTO Fine (Loan_Id, Amount, Paid) VALUES
                                             (2, 10.50, FALSE),
                                             (4, 25.00, TRUE),
                                             (1, 0.00, TRUE),
                                             (3, 0.00, TRUE),
                                             (5, 5.00, FALSE);

SELECT * FROM Author;
SELECT * FROM Author WHERE Author_Id > 2;
SELECT * FROM Author WHERE Country = 'UK';
SELECT Name FROM Author;
SELECT * FROM Author ORDER BY Name desc;

SELECT * FROM Book;
SELECT * FROM Book WHERE Publish_Year > 2000;
SELECT Title FROM Book WHERE Page_Count > 300;
SELECT * FROM Book WHERE Category_Id = 1;

SELECT * FROM Book ORDER BY Publish_Year DESC;
SELECT * FROM Student;
SELECT * FROM Student WHERE Student_Id > 2;
SELECT * FROM Student WHERE Name LIKE 'A%';
SELECT Email FROM Student;
SELECT * FROM Student ORDER BY Name;

SELECT * FROM Staff;
SELECT * FROM Staff WHERE Role = 'Librarian';
SELECT * FROM Staff WHERE Hire_Date > '2021-01-01';
SELECT Name FROM Staff;
SELECT * FROM Staff ORDER BY Hire_Date;

SELECT * FROM Loan;
SELECT * FROM Loan WHERE Loan_Id > 2;
SELECT * FROM Loan WHERE Loan_Date > '2024-01-02';
SELECT * FROM Loan WHERE Student_Id = 1;
SELECT * FROM Loan ORDER BY Due_Date;

SELECT * FROM Return_Table;
SELECT * FROM Return_Table WHERE Return_Id > 2;
SELECT * FROM Return_Table WHERE Return_Date > '2024-01-10';
SELECT * FROM Return_Table WHERE Condition_Notes = 'Good';
SELECT * FROM Return_Table ORDER BY Return_Date;

SELECT * FROM Fine;
SELECT * FROM Fine WHERE Amount > 10;
SELECT * FROM Fine WHERE Paid = FALSE;
SELECT Amount FROM Fine;
SELECT * FROM Fine ORDER BY Amount DESC;

SELECT * FROM Book_Copy;
SELECT * FROM Book_Copy WHERE Status = 'Available';
SELECT * FROM Book_Copy WHERE Copy_Id > 2;
SELECT * FROM Book_Copy WHERE Shelf_Location = 'A1';
SELECT * FROM Book_Copy ORDER BY Copy_Id;

--Book and author
SELECT b.Title, a.Name FROM Book b JOIN Author a ON B.Author_Id = a.Author_Id;

--Loan and student
SELECT s.Name, l.Loan_Date FROM Loan l INNER JOIN Student s ON s.Student_Id = l.Student_Id;

--Loan and Book
SELECT b.Title, l.Loan_Date
FROM Loan l
         INNER JOIN Book_Copy bc ON l.Copy_Id = bc.Copy_Id
         INNER JOIN Book b ON bc.Book_Id = b.Book_Id;

--All books and if there is a author
SELECT b.Title, a.Name
FROM Book b
         LEFT JOIN Author a ON b.Author_Id = a.Author_Id;

--All student and if there is a loan
SELECT s.Name, l.Loan_Id
FROM Loan l
         RIGHT JOIN Student s ON l.Student_Id = s.Student_Id;

--Student who never loan a book
SELECT s.Name FROM Student s LEFT JOIN Loan l ON s.Student_Id=l.Student_Id WHERE l.Loan_Id IS NULL;

--Update
UPDATE Author
SET Country = 'United Kingdom'
WHERE Country = 'UK';

--Between/Order By
SELECT *
FROM Book
WHERE Publish_Year BETWEEN 1950 AND 2020
ORDER BY Publish_Year DESC;

--Insert
INSERT INTO Student (Name, Email, Phone)
VALUES ('Yeni Öğrenci', 'yeni@mail.com', '999999');

--Delete
DELETE FROM Staff
WHERE Staff_Id = 5;

--Count
SELECT COUNT(*) AS Total_Loans
FROM Loan;

--Max
SELECT MAX(Return_Date) AS Last_Return
FROM Return_Table;

--Min
SELECT MIN(Amount) AS Min_Fine
FROM Fine;

--Group By
SELECT Status, COUNT(*) AS Total
FROM Book_Copy
GROUP BY Status;

--Union
SELECT Name FROM Category
UNION
SELECT Name FROM Publisher;

--Union All
SELECT Name FROM Publisher
UNION ALL
SELECT Name FROM Author;

--Avg
SELECT AVG(Page_Count) FROM Book;

--Like
SELECT * FROM Student
WHERE Name LIKE 'A%';

--In
SELECT * FROM Book
WHERE Category_Id IN (1, 2, 3);

--Student who loan most
SELECT s.Name, COUNT(l.Loan_Id) AS total_loans
FROM Student s
         JOIN Loan l ON s.Student_Id = l.Student_Id
GROUP BY s.Name
ORDER BY total_loans DESC
    LIMIT 1;

--Late books
SELECT s.Name, b.Title, l.Due_Date, r.Return_Date
FROM Loan l
         JOIN Student s ON l.Student_Id = s.Student_Id
         JOIN Book_Copy bc ON l.Copy_Id = bc.Copy_Id
         JOIN Book b ON bc.Book_Id = b.Book_Id
         JOIN Return_Table r ON l.Loan_Id = r.Loan_Id
WHERE r.Return_Date > l.Due_Date;

--Book count of every category
SELECT c.Name AS Category, COUNT(b.Book_Id) AS total_books
FROM Category c
         LEFT JOIN Book b ON c.Category_Id = b.Category_Id
GROUP BY c.Name
ORDER BY total_books DESC;

--Student who has max fine
SELECT s.Name, f.Amount
FROM Fine f
         JOIN Loan l ON f.Loan_Id = l.Loan_Id
         JOIN Student s ON l.Student_Id = s.Student_Id
WHERE f.Amount = (SELECT MAX(Amount) FROM Fine);

--Has stock
SELECT b.Title, bc.Status
FROM Book b
         JOIN Book_Copy bc ON b.Book_Id = bc.Book_Id
WHERE bc.Status = 'Available';

--Last returned book
SELECT b.Title, r.Return_Date
FROM Return_Table r
         JOIN Loan l ON r.Loan_Id = l.Loan_Id
         JOIN Book_Copy bc ON l.Copy_Id = bc.Copy_Id
         JOIN Book b ON bc.Book_Id = b.Book_Id
ORDER BY r.Return_Date DESC
    LIMIT 1;