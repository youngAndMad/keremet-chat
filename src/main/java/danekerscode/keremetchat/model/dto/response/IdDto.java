package danekerscode.keremetchat.model.dto.response;

/**
 * This is a record class that represents a generic ID data transfer object (DTO).
 * It is used to encapsulate the ID value of any type <T> into a single object.
 *
 * @param <T> the type of the ID
 */
public record IdDto<T>(T id) {
}