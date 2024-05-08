package danekerscode.keremetchat.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthProcessingException extends RuntimeException {
    private final HttpStatus responseStatus;
    public AuthProcessingException(String msg, HttpStatus status) {
        super(msg);
        this.responseStatus = status;
    }
}
