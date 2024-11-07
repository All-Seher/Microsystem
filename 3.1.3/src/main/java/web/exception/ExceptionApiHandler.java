package web.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String notFoundUser(MethodArgumentNotValidException methodArgumentNotValidException) {
        return Objects.requireNonNull(methodArgumentNotValidException
                        .getBindingResult()
                        .getFieldError())
                .getDefaultMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ValidationException.class)
    public String validInvalid(ValidationException validationException) {

        try {
            return validationException.getCause().getMessage();
        } catch (NullPointerException nullPointerException) {
            return validationException.getMessage();
        }
    }
}
