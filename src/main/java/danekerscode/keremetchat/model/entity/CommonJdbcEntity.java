package danekerscode.keremetchat.model.entity;

public interface CommonJdbcEntity {
    Long getId();

    default boolean isAlreadyCreated() {
        return getId() != null;
    }
}
