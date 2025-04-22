package es.upm.sos.biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PrestamoNotFoundAdvice {
    @ExceptionHandler(PrestamoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage prestamoNotFoundHandler(PrestamoNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(PrestamoOutOfDateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorMessage prestamoOutOfDateHandler(PrestamoOutOfDateException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
