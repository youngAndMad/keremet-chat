package danekerscode.keremetchat.model.exception;

public class InvalidRequestPayloadException extends RuntimeException{
    public InvalidRequestPayloadException(String message) {
        super(message);
    }
}
