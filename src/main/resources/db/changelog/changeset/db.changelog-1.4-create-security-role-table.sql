--liquibase formatted sql
--changeset youngAndMad:create-security-role-table
create table security_role
(
    id                 bigserial primary key,
    created_date       timestamp with time zone,
    last_modified_date timestamp with time zone,
    type               varchar(64)
);
--rollback drop table security_role;

--changeset youngAndMad:insert-base-security-roles
insert into security_role(created_date, type)
values (current_timestamp,'ROLE_APPLICATION_ROOT_ADMIN'),
       (current_timestamp,'ROLE_APPLICATION_MANAGER'),
       (current_timestamp,'ROLE_USER');
--drop table truncate table security_role;

--changeset youngAndMad:create-users-security-roles-table
create table users_security_roles
(
    user_id          bigint,
    security_role_id bigint,
    foreign key (user_id) references users (id),
    foreign key (security_role_id) references security_role (id)
)
--rollback drop table users_security_roles;
