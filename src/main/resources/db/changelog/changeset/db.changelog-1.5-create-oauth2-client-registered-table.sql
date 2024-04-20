--liquibase formatted sql
--changeset youngAndMad:create-oauth2_client_registered-table
CREATE TABLE oauth2_client_registered
(
    registration_id                 VARCHAR(100)  NOT NULL PRIMARY KEY,
    client_id                       VARCHAR(100)  NOT NULL,
    client_secret                   VARCHAR(200),
    client_authentication_method    VARCHAR(100)  NOT NULL,
    authorization_grant_type        VARCHAR(100)  NOT NULL,
    client_name                     VARCHAR(200),
    redirect_uri                    VARCHAR(1000) NOT NULL,
    scopes                          VARCHAR(1000) NOT NULL,
    authorization_uri               VARCHAR(1000),
    token_uri                       VARCHAR(1000) NOT NULL,
    jwk_set_uri                     VARCHAR(1000),
    issuer_uri                      VARCHAR(1000),
    user_info_uri                   VARCHAR(1000),
    user_info_authentication_method VARCHAR(100),
    user_name_attribute_name        VARCHAR(100),
    configuration_metadata          VARCHAR(2000)
);
--rollback DROP TABLE oauth2_client_registered;
