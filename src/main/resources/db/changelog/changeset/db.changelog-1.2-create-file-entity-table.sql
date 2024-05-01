--liquibase formatted sql
--changeset youngAndMad:create-file-entity-table
CREATE TABLE file_entity
(
    id                 bigserial primary key,
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    file_name          varchar,
    target             varchar,
    source             varchar,
    extension          varchar,
    size               bigint not null ,
    path               varchar unique not null
);
--rollback drop table file_entity;
