package danekerscode.keremetchat.model.enums.security;

public enum SecurityRoleType {
    ROLE_APPLICATION_ROOT_ADMIN,
    ROLE_APPLICATION_MANAGER,
    ROLE_USER;

    SecurityRoleType() {
        if (!name().startsWith("ROLE_")) {
            throw new IllegalArgumentException("Name must starts with ROLE_");
        }
    }

}
