--liquibase formatted sql
--changeset youngAndMad:create-table-chat-notification
CREATE TABLE chat_notification
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    inner_id           BIGINT,
    chat_id            BIGINT,
    notification_time  TIMESTAMP WITHOUT TIME ZONE,
    type               VARCHAR(255),
    content            JSONB,
    FOREIGN KEY (chat_id) references chat(id)
);
--rollback drop table chat_notification;
