package danekerscode.keremetchat.model.exception;

import org.springframework.security.core.AuthenticationException;

public class Oauth2ProcessingException extends AuthenticationException {
    public Oauth2ProcessingException(
            String message
    ) {
        super(message);
    }
}
