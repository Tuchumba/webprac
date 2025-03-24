-- Заполнение таблицы users
INSERT INTO users (username, phone, email) VALUES
('Alice Smith', '79123456781', 'alice.smith@example.com'),
('Bob Johnson', '79123456782', 'bob.johnson@example.com'),
('Charlie Brown', '79123456783', 'charlie.brown@example.com'),
('David Wilson', '79123456784', 'david.wilson@example.com'),
('Eve Miller', '79123456785', 'eve.miller@example.com'),
('Frank Davis', '79123456786', 'frank.davis@example.com'),
('Grace Taylor', '79123456787', 'grace.taylor@example.com'),
('Henry Moore', '79123456788', 'henry.moore@example.com');

-- Заполнение таблицы films
INSERT INTO films (title, genre, company, director, year_of_release, description) VALUES
('The Shawshank Redemption', 'Drama', 'Castle Rock Entertainment', 'Frank Darabont', '1994', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.'),
('The Godfather', 'Crime', 'Paramount Pictures', 'Francis Ford Coppola', '1972', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.'),
('The Dark Knight', 'Action', 'Warner Bros.', 'Christopher Nolan', '2008', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.'),
('Pulp Fiction', 'Crime', 'Miramax Films', 'Quentin Tarantino', '1994', 'The lives of two mob hitmen, a boxer, a gangster''s wife, and a pair of diner bandits intertwine in four tales of violence and redemption.'),
('Forrest Gump', 'Drama', 'Paramount Pictures', 'Robert Zemeckis', '1994', 'The presidencies of Kennedy and Johnson, the events of Vietnam, Watergate, and other historical events unfold through the perspective of an Alabama man with an IQ of 75.'),
('Inception', 'Sci-Fi', 'Warner Bros.', 'Christopher Nolan', '2010', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.'),
('The Matrix', 'Sci-Fi', 'Warner Bros.', 'The Wachowskis', '1999', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.'),
('Interstellar', 'Sci-Fi', 'Paramount Pictures', 'Christopher Nolan', '2014', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.');

-- Заполнение таблицы Copy.java
INSERT INTO copies (film_id, type, status, price) VALUES
(1, 'DVD', 'Available', 100.00),
(1, 'tape', 'Rented', 120.50),
(2, 'DVD', 'Available', 110.00),
(2, 'tape', 'Available', 130.00),
(3, 'DVD', 'Rented', 120.00),
(3, 'tape', 'Available', 140.00),
(4, 'DVD', 'Available', 90.50),
(5, 'DVD', 'Available', 100.50),
(6, 'tape', 'Available', 150.00),
(7, 'tape', 'Rented', 140.50);

-- Заполнение таблицы rents
INSERT INTO rents (user_id, copy_id, date_of_transfer, date_of_receipt, actual_date_of_receipt, transfer_amount) VALUES
(1, 1, '2023-10-26', '2023-11-02', '2023-11-01', 100.00),
(2, 3, '2023-10-27', '2023-11-03', '2023-11-03', 120.00),
(3, 7, '2023-10-28', '2023-11-04', NULL, 140.50),
(4, 2, '2023-10-29', '2023-11-05', '2023-11-04', 110.00),
(5, 5, '2023-10-30', '2023-11-06', NULL, 100.50),
(6, 1, '2023-10-31', '2023-11-07', '2023-11-07', 100.00),
(7, 3, '2023-11-01', '2023-11-08', '2023-11-08', 120.00),
(1, 1, '2023-11-05', '2023-11-12', NULL, 100.00);