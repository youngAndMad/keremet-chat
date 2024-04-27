package danekerscode.keremetchat.transport.http.advice;

import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @PostConstruct
    void postConstruct(){
      log.info("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
        return ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        e.getMessage()
                );
    }

    @ExceptionHandler(AuthProcessingException.class)
    public ProblemDetail handleAuthProcessingException(
            AuthProcessingException e
    ) {
        return ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                );
    }
}
