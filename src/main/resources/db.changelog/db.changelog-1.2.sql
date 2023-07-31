--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE notification
(
    id         serial PRIMARY KEY,
    message    varchar(256)                                  NOT NULL,
    created_at timestamp                                     NOT NULL,
    type       varchar(32)                                   NOT NULL,
    status     varchar(32),
    comment    varchar(32),
    project_id int REFERENCES project (id) ON DELETE CASCADE NOT NULL,
    user_id    int REFERENCES users (id) ON DELETE CASCADE   NOT NULL,
    caller_id  int REFERENCES users (id) ON DELETE CASCADE   NOT NULL,
    UNIQUE (project_id, caller_id, type)
);

