--liquibase formatted changeset
--changeset youngAndMad:create-table-verification-token
CREATE TABLE verification_token
(
    id                 BIGSERIAL NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    user_id            BIGINT,
    type               VARCHAR(255),
    expiration_date    TIMESTAMP WITHOUT TIME ZONE,
    value              VARCHAR(255) UNIQUE,
    foreign key (user_id) references users (id)
);
--rollback drop table verification_token;

