INSERT INTO book
    (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1,
        'Sample Book Title 1',
        'Author 1',
        '978-1234567891',
        25,
        'Description 1',
        'https://example.com/book-cover1.jpg',
        false);

INSERT INTO user (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1,
        'alice@gmail.com',
        'password',
        'Alice',
        'Brown',
        '15 Main St, City, Country',
        false);

INSERT INTO shopping_cart (id ,user_id)
VALUES (1, 1);

INSERT INTO cart_item (id, book_id, quantity, shopping_cart_id)
VALUES (1, 1, 1, 1),
       (2, 1, 5, 1);
