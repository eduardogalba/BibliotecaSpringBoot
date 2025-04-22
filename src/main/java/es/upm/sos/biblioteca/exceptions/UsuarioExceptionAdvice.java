package es.upm.sos.biblioteca.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UsuarioExceptionAdvice {
  @ExceptionHandler(UsuarioNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorMessage userNotFoundHandler(UsuarioNotFoundException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(UsuarioNotAllowedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userNotAllowedHandler(UsuarioNotAllowedException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(UsuarioExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userExistsHandler(UsuarioExistsException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(UsuarioHasPrestamosException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ErrorMessage userHasPrestamosHandler(UsuarioHasPrestamosException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleValidationExceptions1(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ErrorMessage(errors.toString());
  }

  @ExceptionHandler(UsuarioPunishmentException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  ErrorMessage userPunishmentHandler(UsuarioPunishmentException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorMessage illegalArgumentHandler(IllegalArgumentException ex) {
    return new ErrorMessage(ex.getMessage());
  }
}