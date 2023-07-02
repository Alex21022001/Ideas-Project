INSERT INTO sec_user (id, username, password, birth_date, firstname, lastname, role, image)
VALUES (1, 'test@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW', '2000-01-01',
        'test', 'test', 'USER', '/image/default-path'),
       (2, 'test2@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW', '2000-01-01',
        'test2', 'test2', 'ADMIN', '/image/default-path');
SELECT setval('sec_user_id_seq', (SELECT MAX(id) FROM sec_user));

INSERT INTO category (id, name)
VALUES (1, 'Electronics');
INSERT INTO category (id, name)
VALUES (2, 'Clothing');
SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));

INSERT INTO test_products (id, name, description, category_id)
VALUES (1, 'Smartphone', 'High-performance mobile device', 1);
INSERT INTO test_products (id, name, description, category_id)
VALUES (2, 'Laptop', 'Powerful and portable computing device', 1);
INSERT INTO test_products (id, name, description, category_id)
VALUES (3, 'Smartwatch', 'Wearable device for tracking health and notifications', 1);

INSERT INTO test_products (id, name, description, category_id)
VALUES (4, 'T-shirt', 'Comfortable and stylish clothing item', 2);
INSERT INTO test_products (id, name, description, category_id)
VALUES (5, 'Jeans', 'Classic denim pants for everyday wear', 2);
INSERT INTO test_products (id, name, description, category_id)
VALUES (6, 'Dress', 'Elegant attire for formal occasions', 2);
SELECT setval('test_products_id_seq', (SELECT MAX(id) FROM test_products));