--liquibase formatted sql
--changeset youngAndMad:create-user-notification-state-table
CREATE TABLE user_notification_state
(
    id                            BIGSERIAL PRIMARY KEY,
    user_id                       BIGINT,
    chat_id                       BIGINT,
    last_received_notification_id BIGINT,
    foreign key (user_id) references users (id) on delete cascade ,
    foreign key (chat_id) references chat (id) on delete cascade
);
--rollback drop table user_notification_state;