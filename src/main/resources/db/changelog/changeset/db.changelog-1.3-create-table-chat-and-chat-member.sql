--liquibase formatted sql
--changeset youngAndMad:create-table-chat
CREATE TABLE chat
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    type               VARCHAR(255),
    avatar_url         VARCHAR(255),
    name               VARCHAR(255)
);
--rollback DROP TABLE chat;

--changeset youngAndMad:create-table-chat_member
CREATE TABLE chat_member
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    user_id            BIGINT,
    role               VARCHAR(255),
    chat_id            BIGINT,
    foreign key (user_id) references users (id),
    foreign key (chat_id) references chat (id)
);
--rollback DROP TABLE chat_member;

--changeset youngAndMad:create-table-chat_message
CREATE TABLE chat_avatars
(
    chat_id    BIGINT NOT NULL,
    file_entity_id BIGINT NOT NULL,
    foreign key (chat_id) references chat (id),
    foreign key (file_entity_id) references file_entity (id)
);
