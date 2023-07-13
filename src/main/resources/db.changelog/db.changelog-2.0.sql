--liquibase formatted sql

--changeset alexsitiy:1
ALTER TABLE user_comment
    ADD COLUMN commented_at timestamp NOT NULL default now();

--changeset alexsitiy:2
CREATE TABLE reaction
(
    id serial PRIMARY KEY,
    likes int NOT NULL DEFAULT 0,
    dislikes int NOT NULL DEFAULT 0,
    project_id int REFERENCES project(id) ON DELETE CASCADE UNIQUE NOT NULL
);

--changeset alexsitiy:3
ALTER TABLE users
    ADD COLUMN avatar varchar(128) NOT NULL DEFAULT 'defaultAvatar.png'


