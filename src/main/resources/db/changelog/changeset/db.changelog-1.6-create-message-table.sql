--liquibase formatted sql
--changeset youngAndMad:create-message-table
create table message
(
    id                 bigserial primary key,
    created_date       timestamp with time zone,
    last_modified_date timestamp with time zone,
    sent_at            timestamp with time zone,
    content            varchar,
    chat_id            bigint,
    sender_id          bigint,
    parent_id          bigint,
    deleted            boolean not null,
    edited             boolean not null,
    foreign key (chat_id) references chat (id),
    foreign key (sender_id) references chat_member (id),
    foreign key (parent_id) references message (id)
);
--rollback drop table message;

--changeset youngAndMad:create-message-files-table
create table message_files
(
    message_id bigint not null ,
    files_id   bigint not null unique,
    foreign key (message_id) references message (id),
    foreign key (files_id) references file_entity (id)
);
--rollback drop table message_files;