--liquibase formatted sql
--changeset youngAndMad:create-otp-table
CREATE TABLE otp
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    otp                VARCHAR(255),
    expire_date        TIMESTAMP WITHOUT TIME ZONE,
    user_id            BIGINT,
    sent_date          TIMESTAMP WITHOUT TIME ZONE,
    foreign key (user_id) references users(id)
);
--rollback DROP TABLE otp;
