CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title varchar(255) not null,
    author varchar(255) not null,
    published DATE
);
CREATE SEQUENCE books_seq START 1;
ALTER TABLE books ALTER COLUMN id SET DEFAULT nextval('books_seq');
ALTER SEQUENCE books_seq OWNED BY books.id;

INSERT INTO books (title, author, published) VALUES 
('Harry Potter 1', 'JK. Rowling', '2002-10-10'),
('Harry Potter e a pedra filosofal', 'JK. Rowling', '2002-12-10'),
('Harry Potter e os talismas da morte part 1', 'JK. Rowling', '2006-12-10'),
('Book 2', 'book2 author', '2003-12-10'),
('Book 3', 'book3 author', '2003-06-21');
