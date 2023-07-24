--liquibase formatted sql

--changeset alexsitiy:1
ALTER TABLE project_status
    ADD COLUMN expert_id int REFERENCES users (id);

