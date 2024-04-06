-- liquibase formatted sql
--changeset youngAndMad:create-users-table
CREATE TABLE users
(
    id                  BIGSERIAL PRIMARY KEY,
    created_date        TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date  TIMESTAMP WITHOUT TIME ZONE,
    email               VARCHAR(255),
    email_verified      BOOLEAN,
    username            VARCHAR(255),
    password            VARCHAR(255),
    auth_type           VARCHAR(255),
    image_url           VARCHAR(255),
    profile_description VARCHAR(255),
    is_active           BOOLEAN,
    role                VARCHAR(255)
);
--rollback DROP TABLE users;
