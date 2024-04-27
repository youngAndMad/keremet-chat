--liquibase formatted sql
--changeset youngAndMad:create-oauth2_client_registration-table
CREATE TABLE oauth2_client_registration
(
    registration_id                 VARCHAR(100) PRIMARY KEY,
    client_id                       VARCHAR(100),
    client_secret                   VARCHAR(200),
    client_authentication_method    VARCHAR(100),
    authorization_grant_type        VARCHAR(100),
    client_name                     VARCHAR(200),
    redirect_uri                    VARCHAR(1000),
    scopes                          VARCHAR(1000),
    authorization_uri               VARCHAR(1000),
    token_uri                       VARCHAR(1000),
    jwk_set_uri                     VARCHAR(1000),
    issuer_uri                      VARCHAR(1000),
    user_info_uri                   VARCHAR(1000),
    user_info_authentication_method VARCHAR(100),
    user_name_attribute_name        VARCHAR(100),
    configuration_metadata          VARCHAR(2000),
    provider_name                   VARCHAR(100)
);
--rollback DROP TABLE oauth2_client_registration;
