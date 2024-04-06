package danekerscode.keremetchat.authserver.model.exception;

import org.springframework.http.HttpStatus;

public class AuthProcessingException extends RuntimeException {
    private final HttpStatus responseStatus;
    public AuthProcessingException(String msg, HttpStatus status) {
        super(msg);
        this.responseStatus = status;
    }
}
