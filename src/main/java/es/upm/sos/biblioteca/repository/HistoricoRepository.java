package es.upm.sos.biblioteca.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.upm.sos.biblioteca.model.Historico;
import jakarta.transaction.Transactional;

public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    
    List<Historico> findByUsuarioId(int usuarioId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE historico_prestamo SET fechaDevolucion = CURRENT_TIMESTAMP, devuelto = TRUE WHERE usuarioId = :usuarioId AND isbnLibro = :isbn", nativeQuery = true)
    void devolverHistorico(int usuarioId, String isbn);

    @Modifying
    @Transactional
    @Query(value = "UPDATE historico_prestamo SET fechaDevolucion = CURRENT_TIMESTAMP WHERE usuarioId = :usuarioId", nativeQuery = true)
    void updateFechaDevolucion(int usuarioId);

    @Query(value = "SELECT * FROM historico_prestamo WHERE usuarioId = :usuarioId AND devuelto = :devuelto ORDER BY fechaDevolucion ASC", nativeQuery = true)
    Page<Historico> findByUsuarioIdAndDevuelto(int usuarioId, boolean devuelto, Pageable pageable);
    

    @Query(value = "SELECT * FROM historico_prestamo WHERE usuarioId = :usuarioId", nativeQuery = true)
    Page<Historico> findHistoricoByUsuarioId(int usuarioId, Pageable pageable);

}
