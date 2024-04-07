--liquibase formatted sql
--changeset youngAndMad:create-roles-table
CREATE TABLE roles
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    name               SMALLINT,
    user_id            BIGINT,
    foreign key (user_id) references users (id)
);
--rollback DROP TABLE roles;
