package es.upm.sos.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.upm.sos.biblioteca.model.Libro;
import jakarta.transaction.Transactional;

public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Buscar si existe por ISBN
    Optional<Libro> findByIsbn(String isbn);

    @Query(value = "SELECT * FROM libro ORDER BY libroId ASC", nativeQuery = true)
    Page<Libro> findAllOrdered(Pageable pageable);

    @Query(value = "SELECT * FROM libro WHERE titulo LIKE %:titulo% ORDER BY libroId ASC", nativeQuery = true)
    Page<Libro> findByTituloContainingOrdered(String titulo, Pageable pageable);

    @Query(value = "SELECT * FROM libro WHERE volumenes > prestados ORDER BY libroId ASC", nativeQuery = true)
    Page<Libro> findLibrosDisponibles(Pageable pageable);    

    @Query(value = "SELECT COUNT(*) FROM libro WHERE libroId = :libroId AND volumenes > prestados", nativeQuery = true)
    Optional<Integer> librosDisponibles(int libroId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE libro SET prestados = prestados + 1 WHERE libroId = :libroId", nativeQuery = true)
    void reservarLibro(int libroId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE libro SET prestados = prestados - 1 WHERE libroId = :libroId AND prestados > 0", nativeQuery = true)
    void devolverLibro(int libroId);
}
