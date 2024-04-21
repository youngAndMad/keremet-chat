--liquibase formatted sql
--changeset youngAndMad:create-file-entity-table
create table file_entity
(
    id                 bigserial primary key,
    created_date       timestamp with time zone,
    last_modified_date timestamp with time zone,
    file_name          varchar(255),
    extension          varchar(255),
    size               bigint not null
);
--rollback drop table file_entity;
