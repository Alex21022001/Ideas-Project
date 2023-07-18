--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE project_status
(
    id         serial PRIMARY KEY,
    status     varchar(16)                                   NOT NULL,
    project_id int REFERENCES project (id) ON DELETE CASCADE NOT NULL UNIQUE
);

--changeset alexsitiy:2
ALTER TABLE project
    DROP COLUMN status

