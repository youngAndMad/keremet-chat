-- liquibase formatted sql
--changeset youngAndMad:create-users-table
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    email              VARCHAR(255),
    email_verified     BOOLEAN,
    username           VARCHAR(255),
    password           VARCHAR(255),
    provider       VARCHAR(255),
    email_confirmed    BOOLEAN,
    is_active          BOOLEAN
);
--rollback DROP TABLE users;
