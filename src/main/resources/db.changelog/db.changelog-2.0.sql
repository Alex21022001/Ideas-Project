--liquibase formatted sql

--changeset alexsitiy:1
ALTER TABLE user_comment
    ADD COLUMN commented_at timestamp NOT NULL default now();

