package danekerscode.keremetchat.model.exception;

import danekerscode.keremetchat.model.dto.KeyPair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("Entity %s with id %s not found", entityClass.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> entityClass, KeyPair<String> errorDetails) {
        super(String.format("Entity %s with %s %s not found", entityClass.getSimpleName(), errorDetails.key(), errorDetails.value()));
    }
}
