--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE users
(
    id        serial PRIMARY KEY,
    firstname varchar(32)  NOT NULL,
    lastname  varchar(32)  NOT NULL,
    role      varchar(32)  NOT NULL,
    username     varchar(64)  NOT NULL UNIQUE,
    password  varchar(128) NOT NULL
);

--changeset alexsitiy:2
CREATE TABLE project
(
    id          serial PRIMARY KEY,
    title       varchar(32)                                 NOT NULL UNIQUE,
    description text                                        NOT NULL,
    image_path  varchar(128)                                NOT NULL,
    docs_path   varchar(128),
    status      varchar(16)                                 NOT NULL DEFAULT 'WAITING',
    user_id     int REFERENCES users (id) ON DELETE CASCADE NOT NULL
);

--changeset alexsitiy:3
CREATE TABLE user_comment
(
    id           serial PRIMARY KEY,
    user_id      int REFERENCES users (id)   NOT NULL,
    project_id   int REFERENCES project (id) NOT NULL,
    comment_type varchar(16)                 NOT NULL,
    UNIQUE (user_id, project_id)
);
