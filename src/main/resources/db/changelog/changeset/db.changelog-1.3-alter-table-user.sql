--liquibase formatted sql
--changeset kaliaskaruly_d:alter-table-user-drop-column-auth-type
alter table users drop column auth_type;

--changeset kaliaskaruly_d:alter-table-user-add-column-auth-type-id
alter table users add column auth_type_id bigint;
--rollback alter table users drop column auth_type_id;

--changeset kaliaskaruly_d:alter-table-user-add-column-fk-auth-type-id
alter table users add constraint fk_users_auth_type_id
    foreign key (auth_type_id) references auth_type(id);
--rollback alter table users drop constraint fk_users_auth_type_id;
