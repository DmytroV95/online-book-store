INSERT INTO user (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'alice@gmail.com', 'password', 'Alice', 'Brown', '15 Main St, City, Country', false);

INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER');

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);

INSERT INTO book
    (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1,
        'Sample Book Title 1',
        'Author 1',
        '978-1234567891',
        150,
        'Description 1',
        'https://example.com/book-cover1.jpg',
        false),

       (2,
        'Sample Book Title 2',
        'Author 2',
        '978-1234567892',
        150,
        'Description 2',
        'https://example.com/book-cover2.jpg',
        false);

INSERT INTO category (id, name, description, is_deleted)
VALUES (1, 'Science Fiction', 'Science Fiction category description', false),
       (2, 'Mystery', 'Mystery category description', false);

INSERT INTO book_category (book_id, category_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO shopping_cart (id, user_id)
VALUES (1, 1);

INSERT INTO cart_item (id, book_id, quantity, shopping_cart_id)
VALUES (1, 1, 5, 1),
       (2, 2, 10, 1);

INSERT INTO orders (id, user_id, status, total, order_date, shipping_address, is_deleted)
VALUES (1, 1, 'PENDING', 150, '2023-10-18T23:39:38', '15 Main St, City, Country', false),
       (2, 1, 'PENDING', 150, '2023-10-18T23:39:39', '15 Main St, City, Country', false);

INSERT INTO orders_item (id, order_id, book_id, quantity, price, is_deleted)
VALUES (1, 1, 1, 5, 150, false),
       (2, 2, 2, 10, 150, false);
