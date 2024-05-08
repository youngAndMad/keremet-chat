package danekerscode.keremetchat.transport.http.advice;

import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.Oauth2ProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @PostConstruct
    void postConstruct() {
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
    ProblemDetail handleAuthProcessingException(
            AuthProcessingException e
    ) {
        return ProblemDetail
                .forStatusAndDetail(
                        e.getResponseStatus(),
                        e.getMessage()
                );
    }

    @ExceptionHandler(Oauth2ProcessingException.class)
    ResponseEntity<ProblemDetail> handleOauth2ProcessingException(
            Oauth2ProcessingException e
    ) {
        return new ResponseEntity<>(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        ), HttpStatus.UNAUTHORIZED);
    }
}
