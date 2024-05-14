--liquibase formatted sql
--changeset youngAndMad:create-table-chat-notification
CREATE TABLE chat_notification
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    inner_id           BIGINT,
    sender_id          BIGINT NOT NULL,
    chat_id            BIGINT NOT NULL,
    notification_time  TIMESTAMP WITHOUT TIME ZONE,
    type               VARCHAR(64),
    content            JSONB,
    FOREIGN KEY (chat_id) references chat (id),
    FOREIGN KEY (sender_id) references users (id)
);
--rollback drop table chat_notification;
