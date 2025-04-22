package es.upm.sos.biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LibroNotFoundAdvice {
  @ExceptionHandler(LibroNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorMessage userNotFoundHandler(LibroNotFoundException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(LibroNotAllowedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userNotAllowedHandler(LibroNotAllowedException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(LibroExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userExistsHandler(LibroExistsException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(LibroInPrestamosException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userInPrestamosHandler(LibroInPrestamosException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(NoLibrosDisponiblesException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage noHayLibrosDisponiblesHandler(NoLibrosDisponiblesException ex) {
    return new ErrorMessage(ex.getMessage());
  }
}
