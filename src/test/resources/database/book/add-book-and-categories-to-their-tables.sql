INSERT INTO book
    (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1,
        'Sample Book Title 3',
        'Author 3',
        '978-1234567893',
        24.99,
        'Description 3',
        'https://example.com/book-cover3.jpg',
        false),

       (2,
        'Sample Book Title 4',
        'Author 4',
        '978-1234567894',
        15.99,
        'Description 4',
        'https://example.com/book-cover4.jpg',
        false);

INSERT INTO category (id, name, description, is_deleted)
VALUES
    (1, 'Science Fiction', 'Science Fiction category description.', false),
    (2, 'Mystery', 'Mystery category description.', false);


INSERT INTO book_category (book_id, category_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);
