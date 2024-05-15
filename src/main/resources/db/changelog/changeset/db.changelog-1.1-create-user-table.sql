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
    auth_type_id       bigint not null,
    email_confirmed    BOOLEAN,
    is_active          BOOLEAN,
    foreign key (auth_type_id) references auth_type (id)
);
--rollback DROP TABLE users;
