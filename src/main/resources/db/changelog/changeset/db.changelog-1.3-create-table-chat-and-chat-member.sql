--liquibase formatted sql
--changeset youngAndMad:create-table-chat
CREATE TABLE chat
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    type               VARCHAR(255),
    avatar_url         VARCHAR(255),
    settings_id        BIGINT NOT NULL,
    name               VARCHAR(255)
);
--rollback DROP TABLE chat;

--changeset youngAndMad:create-table chat_settings
CREATE TABLE chat_settings
(
    id                          BIGSERIAL PRIMARY KEY,
    created_date                TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date          TIMESTAMP WITHOUT TIME ZONE,
    chat_id                     BIGINT,
    everyone_can_invite_members BOOLEAN,
    members_list_is_available   BOOLEAN,
    admins_can_edit_settings    BOOLEAN
);
--rollback drop table chat_settings;

--changeset youngAndMad:alter-table-chat_settings-add-fk-to-chat
ALTER TABLE chat_settings
    ADD CONSTRAINT FK_CHATSETTINGS_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id);
--rollback alter table chat_settings drop constraint  FK_CHATSETTINGS_ON_CHAT;

--changeset youngAndMad:alter-table-chat-add-fk-to-chat_settings
ALTER TABLE chat
    ADD CONSTRAINT FK_CHAT_ON_CHATSETTINGS FOREIGN KEY (settings_id) REFERENCES chat_settings (id);
--rollback alter table chat drop constraint  FK_CHAT_ON_CHATSETTINGS;

--changeset youngAndMad:create-table-chat_member
CREATE TABLE chat_member
(
    id                            BIGSERIAL PRIMARY KEY,
    created_date                  TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date            TIMESTAMP WITHOUT TIME ZONE,
    user_id                       BIGINT,
    role                          VARCHAR(255),
    chat_id                       BIGINT,
    last_received_notification_id BIGINT,
    foreign key (user_id) references users (id),
    foreign key (chat_id) references chat (id)
);
--rollback DROP TABLE chat_member;

--changeset youngAndMad:create-table-chat_message
CREATE TABLE chat_avatars
(
    chat_id        BIGINT NOT NULL,
    file_entity_id BIGINT NOT NULL,
    foreign key (chat_id) references chat (id),
    foreign key (file_entity_id) references file_entity (id)
);
