INSERT INTO book
    (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1,
        'Sample Book Title 1',
        'Author 1',
        '978-1234567891',
        25,
        'Description 1',
        'https://example.com/book-cover1.jpg',
        false),

       (2,
        'Sample Book Title 2',
        'Author 2',
        '978-1234567892',
        16,
        'Description 2',
        'https://example.com/book-cover2.jpg',
        false),

       (3,
        'Sample Book Title 3',
        'Author 3',
        '978-1334567893',
        160,
        'Description 3',
        'https://example.com/book-cover3.jpg',
        false);

INSERT INTO category (id, name, description, is_deleted)
VALUES (1, 'Science Fiction', 'Science Fiction category description', false),
       (2, 'Mystery', 'Mystery category description', false);


INSERT INTO book_category (book_id, category_id)
VALUES (1, 1),
       (2, 2),
       (3, 2);
