INSERT INTO users (id, username, password, firstname, lastname, role, avatar)
VALUES (1, 'test1@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'Test', 'Test', 'USER', 'test1Avatar.png'),
       (2, 'test2@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'TestExpert', 'TestExpert', 'EXPERT', 'test2Avatar.png'),
       (3, 'test3@gmail.com', '{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW',
        'Test3', 'Test3', 'USER', 'test3Avatar.png');
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO project(id, title, description, image_path, docs_path, created_at, user_id)
VALUES (1, 'test1', 'test1-description', 'test1.png', 'test1.pdf', '2023-07-16 08:49:23.075000',
        (SELECT u.id FROM users u WHERE id = 1)),
       (2, 'test2', 'test2-description', 'test2.png', 'test2.pdf', '2023-07-17 08:49:23.075000',
        (SELECT u.id FROM users u WHERE id = 1)),
       (3, 'test3', 'test3-description', 'test3.png', 'test3.pdf', '2023-07-18 08:49:23.075000',
        (SELECT u.id FROM users u WHERE id = 2));
SELECT setval('project_id_seq', (SELECT MAX(id) FROM project));

INSERT INTO project_status(id, status, project_id, version, expert_id)
VALUES (1, 'IN_PROGRESS', 1, 0, null),
       (2, 'REJECTED', 2, 1, 2),
       (3, 'ACCEPTED', 3, 1, 2);
SELECT setval('project_status_id_seq', (SELECT MAX(id) FROM project_status));

INSERT INTO comment(id, user_id, project_id, comment_type, commented_at)
VALUES (1, 1, 1, 'LIKE', '2023-07-09 18:33:06.981997'),
       (2, 1, 2, 'LIKE', '2023-07-09 18:34:06.981997'),
       (3, 1, 3, 'DISLIKE', '2023-07-09 18:35:06.981997'),
       (4, 2, 1, 'LIKE', '2023-07-09 18:35:06.981997'),
       (5, 2, 2, 'DISLIKE', '2023-07-09 18:36:06.981997'),
       (6, 2, 3, 'DISLIKE', '2023-07-09 18:36:06.981997');
SELECT setval('comment_id_seq', (SELECT MAX(id) FROM comment));

INSERT INTO project_reaction(id, likes, dislikes, project_id)
VALUES (1, 2, 0, 1),
       (2, 1, 1, 2),
       (3, 0, 2, 3);
SELECT setval('project_reaction_id_seq', (SELECT MAX(id) FROM project_reaction));

INSERT INTO notification(id, message, created_at, type, status, comment, project_id, user_id, caller_id)
VALUES (1, 'Your Project: [test2] was rejected by TestExpert TestExpert',
        '2023-07-09 18:36:06.981997', 'STATUS', 'REJECTED', null, 2, 1, 2),
       (2, 'Your Project: [test3] was accepted by TestExpert TestExpert',
        '2023-07-09 18:36:06.981997', 'STATUS', 'ACCEPTED', null, 3, 2, 2),
       (3, 'Your Project: [test1] was liked by TestExpert TestExpert',
        '2023-07-09 18:36:06.981997', 'COMMENT', 'LIKE', null, 1, 1, 2),
       (4, 'Your Project: [test2] was disliked by TestExpert TestExpert',
        '2023-07-09 18:36:06.981997', 'COMMENT', 'DISLIKE', null, 2, 1, 2),
       (5, 'Your Project: [test3] was disliked by TestExpert TestExpert',
        '2023-07-09 18:36:06.981997', 'COMMENT', 'DISLIKE', null, 3, 2, 2);
SELECT setval('notification_id_seq', (SELECT MAX(id) FROM notification));
