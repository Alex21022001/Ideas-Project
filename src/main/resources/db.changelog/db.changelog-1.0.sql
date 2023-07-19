--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE users
(
    id        serial PRIMARY KEY,
    firstname varchar(32)  NOT NULL,
    lastname  varchar(32)  NOT NULL,
    role      varchar(32)  NOT NULL,
    username  varchar(64)  NOT NULL UNIQUE,
    password  varchar(128) NOT NULL,
    avatar    varchar(128) NOT NULL
);

--changeset alexsitiy:2
CREATE TABLE project
(
    id          serial PRIMARY KEY,
    title       varchar(32)                                 NOT NULL UNIQUE,
    description text                                        NOT NULL,
    image_path  varchar(128)                                NOT NULL,
    docs_path   varchar(128),
    created_at  timestamp                                   NOT NULL,
    user_id     int REFERENCES users (id) ON DELETE CASCADE NOT NULL
);

--changeset alexsitiy:3
CREATE TABLE project_reaction
(
    id         serial PRIMARY KEY,
    likes      int                                                  NOT NULL DEFAULT 0,
    dislikes   int                                                  NOT NULL DEFAULT 0,
    project_id int REFERENCES project (id) ON DELETE CASCADE UNIQUE NOT NULL
);

--changeset alexsitiy:4
CREATE TABLE project_status
(
    id         serial PRIMARY KEY,
    status     varchar(16)                                   NOT NULL,
    project_id int REFERENCES project (id) ON DELETE CASCADE NOT NULL UNIQUE
);

--changeset alexsitiy:5
CREATE TABLE comment
(
    id           serial PRIMARY KEY,
    user_id      int REFERENCES users (id) on DELETE CASCADE   NOT NULL,
    project_id   int REFERENCES project (id) on DELETE CASCADE NOT NULL,
    comment_type varchar(16)                                   NOT NULL,
    commented_at timestamp                                     NOT NULL,
    UNIQUE (user_id, project_id)
);
