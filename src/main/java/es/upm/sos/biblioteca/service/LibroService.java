package es.upm.sos.biblioteca.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.upm.sos.biblioteca.exceptions.LibroInPrestamosException;
import es.upm.sos.biblioteca.model.Libro;
import es.upm.sos.biblioteca.repository.LibroRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibroService {

    private final LibroRepository repository;

    public boolean buscarPorISBN(String isbn) {
        Optional<Libro> libro = repository.findByIsbn(isbn);
        return libro.isPresent();
    }

    public Libro crearLibro(Libro libro) {
        return repository.save(libro);
    }

    public Optional<Libro> buscarPorId(int id) {
        return repository.findById(id);
    }

    public boolean existeLibroPorId(int id) {
        return repository.existsById(id);
    }

    public Page<Libro> buscarLibros(String pattern, boolean disponible, int page, int size) {
        // Crear el objeto Pageable usando el número de página, el tamaño y el campo por
        // el que se ordena (name,desc)
        Pageable paginable = PageRequest.of(page, size);
        if (!pattern.isEmpty()) {
            return repository.findByTituloContaining(pattern, paginable);
        } else if (disponible){
            return repository.findLibrosDisponibles(paginable);
        } else {
            return repository.findAll(paginable);
        }
    }

    public void eliminarLibro(int id) {
        try {
            repository.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            throw new LibroInPrestamosException(id);
        }
        
    }

}
