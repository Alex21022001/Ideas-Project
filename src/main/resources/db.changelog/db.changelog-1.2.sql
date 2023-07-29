--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE notification
(
    id         serial PRIMARY KEY,
    message    varchar(256)                                NOT NULL,
    created_at timestamp                                   NOT NULL,
    type       varchar(32)                                 NOT NULL,
    project_id int REFERENCES project (id)                 NOT NULL,
    user_id    int REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    caller_id  int REFERENCES users (id)                   NOT NULL,
    UNIQUE (project_id, caller_id, type)
);

