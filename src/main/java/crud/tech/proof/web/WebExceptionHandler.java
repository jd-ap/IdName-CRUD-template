package crud.tech.proof.web;

import crud.tech.proof.model.ErrorDto;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handleGeneralException(Exception exception) {
        log.error("internal_error_{}: {}", exception.getClass().getName(), exception.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handleNullValueException(NullPointerException exception) {
        log.error("null_fatal_error", exception);
    }

    @ExceptionHandler(EmptyStackException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void handleEmptyStackException(EmptyStackException exception) {
        log.info("no content");
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleNoSuchElementException(NoSuchElementException exception) {
        log.info("no_such_element_error: {}", exception.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException exception) {
        log.warn("parameter_miss_error.{}: {}", exception.getName(), exception.getMostSpecificCause().getMessage());

        return new ErrorDto()
                .type("parameter-miss-error")
                .title("failed %s: %s.%s".formatted(exception.getPropertyName(), exception.getParameter().getParameterName(), exception.getName()))
                .addDetailItem(exception.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("parameter_error.{}: {}", exception.getBindingResult().getObjectName(), exception.getMessage());

        return new ErrorDto()
                .type("parameter-error")
                .title(exception.getBindingResult().getObjectName().concat(" failed"))
                .detail(exception.getBindingResult().getAllErrors().stream()
                        .map(o -> "[".concat(Objects.requireNonNull(o.getCodes())[0]).concat("] ").concat(Objects.requireNonNull(o.getDefaultMessage())))
                        .toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("database_{}_error: {}", exception.getClass().getName(), exception.getMessage(), exception);

        return new ErrorDto()
                .type("database-constraint-error")
                .title(exception.getMessage())
                .detail(exception.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).toList());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handlePersistenceException(PersistenceException exception) {
        log.error("database_error: {}", exception.getLocalizedMessage());
    }

}
