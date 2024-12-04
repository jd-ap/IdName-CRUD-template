package crud.tech.proof.web;

import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handleGeneralException(Exception exception) {
        log.error("internal_error: {}", exception.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handleNullValueException(NullPointerException exception) {
        log.error("null_fatal_error", exception);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleNoSuchElementException(NoSuchElementException exception) {
        log.debug("no_such_element_error: {}", exception.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException exception) {
        log.debug("parameter_error.{}: {}", exception.getName(), exception.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        log.error("parameter_error.{}: {}", manve.getBindingResult().getObjectName(), manve.getMessage());
/*
        return ErrorDTO.builder()
                .message(manve.getBindingResult().getObjectName().concat(" failed"))
                .details(manve.getBindingResult().getAllErrors().stream()
                        .map(o -> "[".concat(Objects.requireNonNull(o.getCodes())[0]).concat("] ").concat(Objects.requireNonNull(o.getDefaultMessage())))
                        .toList())
                .build();

 */
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleConstraintViolationException(ConstraintViolationException cve) {
        log.error("database_error: {}", cve.getMessage());
/*
        return ErrorDTO.builder()
                .details(cve.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).toList())
                .build();

 */
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handlePersistenceException(PersistenceException exception) {
        log.error("database_error: {}", exception.getLocalizedMessage());
    }

}
