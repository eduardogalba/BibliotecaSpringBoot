package es.upm.sos.biblioteca.exceptions;

import es.upm.sos.biblioteca.model.Libro;

public class LibroExistsException extends RuntimeException {
  public LibroExistsException(Libro libro) {
    super(String.format("Libro con titulo %s autores %s  isbn %s ya existe", libro.getTitulo(), libro.getAutores(), libro.getIsbn()));
    
  }
}
