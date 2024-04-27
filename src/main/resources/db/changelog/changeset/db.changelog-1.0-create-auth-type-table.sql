--liquibase formatted sql
--changeset youngAndMad:create-auth-type-table
create table auth_type(
    id bigserial primary key,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    name varchar
);
--rollback drop table auth_type;

--changeset youngAndMad:insert-default-auth-type
insert into auth_type(created_date, name) values (current_timestamp, 'MANUAL');
--rollback delete from auth_type where name = 'MANUAL';
