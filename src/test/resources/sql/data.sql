INSERT INTO users (id, username, password, firstname, lastname, role)
VALUES (1, 'test1@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'Test', 'Test', 'USER'),
       (2, 'test2@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'TestExpert', 'TestExpert', 'EXPERT');
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
