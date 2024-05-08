package danekerscode.keremetchat.model.exception;

public class Oauth2ProcessingException extends RuntimeException {
    public Oauth2ProcessingException(
            String message
    ) {
        super(message);
    }
}
