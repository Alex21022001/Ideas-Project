--liquibase formatted sql

--changeset alexsitiy:1
CREATE TABLE notification
(
    id serial PRIMARY KEY,
    message varchar(256) NOT NULL,
    created_at timestamp NOT NULL,
    user_id int REFERENCES users(id) NOT NULL
);

