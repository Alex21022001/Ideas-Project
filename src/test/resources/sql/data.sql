INSERT INTO users (id, username, password, firstname, lastname, role)
VALUES (1, 'test1@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'Test', 'Test', 'USER'),
       (2, 'test2@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'TestExpert', 'TestExpert', 'EXPERT');
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO project(id, title, description, image_path, docs_path, status, user_id)
VALUES (1, 'test1', 'test1-description', 'test1.png', 'test1.pdf', 'WAITING', (SELECT u.id FROM users u WHERE id = 1)),
       (2, 'test2', 'test2-description', 'test2.png', 'test2.pdf', 'ACCEPTED', (SELECT u.id FROM users u WHERE id = 1)),
       (3, 'test3', 'test3-description', 'test3.png', 'test3.pdf', 'REJECTED', (SELECT u.id FROM users u WHERE id = 2));
SELECT setval('project_id_seq',(SELECT MAX(id) FROM project));