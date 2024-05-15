package danekerscode.keremetchat.transport.http.advice;

import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.Oauth2ProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        var problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatusCode.valueOf(404),
                        collectBindingErrors(ex.getBindingResult())
                );
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    private String collectBindingErrors(BindingResult br) {
        return br.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + "-" + fieldError.getDefaultMessage())
                .collect(Collectors.joining());
    }
}
