package es.upm.sos.biblioteca.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.upm.sos.biblioteca.model.Prestamo;
import es.upm.sos.biblioteca.model.PrestamoId;
import jakarta.transaction.Transactional;

public interface PrestamoRepository extends JpaRepository<Prestamo, PrestamoId> {

    // Buscar todos los préstamos de un libro específico
    List<Prestamo> findByLibroLibroId(int libroId);
    
    // Buscar todos los préstamos de un usuario específico
    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId", nativeQuery = true)
    List<Prestamo> findPrestamosUsuario(int usuarioId);

    // Buscar todos los préstamos de un usuario específico
    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId ORDER BY fechaPrestamo ASC", nativeQuery = true)
    List<Prestamo> findPrestamosUsuarioPorFechaPrestamo(int usuarioId);

    // Actualizar la fecha de devolución de un préstamo
    @Modifying
    @Transactional
    @Query(value = "UPDATE prestamo SET fechaDevolucion = CURRENT_TIMESTAMP + INTERVAL '14 days' WHERE usuarioId = :usuarioId AND libroId = :libroId", nativeQuery = true)
    void updateFechaDevolucion(int usuarioId, int libroId);

    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId", nativeQuery = true)
    Page<Prestamo> findPrestamosByUsuarioId(int usuarioId, Pageable pageable);

    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId AND fechaPrestamo < :fechaPrestamo", nativeQuery = true)
    Page<Prestamo> findPrestamosByUsuarioIdBeforeFechaPrestamo(int usuarioId, LocalDateTime fechaPrestamo, Pageable pageable);

    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId AND fechaPrestamo > :fechaPrestamo", nativeQuery = true)
    Page<Prestamo> findPrestamosByUsuarioIdAfterFechaPrestamo(int usuarioId, LocalDateTime fechaPrestamo, Pageable pageable);

    @Query(value = "SELECT * FROM prestamo WHERE usuarioId = :usuarioId AND fechaPrestamo BETWEEN :startDate AND :endDate", nativeQuery = true)
    Page<Prestamo> findPrestamosByUsuarioIdBetweenFechas(int usuarioId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
