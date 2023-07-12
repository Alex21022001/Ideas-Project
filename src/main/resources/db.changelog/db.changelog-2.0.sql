--liquibase formatted sql

--changeset alexsitiy:1
ALTER TABLE user_comment
    ADD COLUMN commented_at timestamp NOT NULL default now();

--changeset alexsitiy:2
ALTER TABLE project
    ADD COLUMN likes int NOT NULL DEFAULT 0;

--changeset alexsitiy:3
ALTER TABLE project
    ADD COLUMN dislikes int NOT NULL DEFAULT 0;

