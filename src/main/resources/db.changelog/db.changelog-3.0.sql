--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE revision
(
    id        serial PRIMARY KEY,
    timestamp bigint      NOT NULL,
    username  varchar(64) NOT NULL
);

--changeset alexsitiy:2
CREATE TABLE project_aud
(
    id          int,
    title       varchar(32),
    description text,
    image_path  varchar(128),
    docs_path   varchar(128),
    status      varchar(16),
    created_at  timestamp,
    rev         int REFERENCES revision (id),
    revtype     smallint
);


