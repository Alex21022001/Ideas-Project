--liquibase formatted sql

--changeset alexsitiy:1
ALTER TABLE project
    ADD COLUMN created_at timestamp NOT NULL DEFAULT now();


