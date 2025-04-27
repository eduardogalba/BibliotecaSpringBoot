package es.upm.sos.biblioteca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.upm.sos.biblioteca.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Buscar si existe por correo
    boolean existsByCorreo(String nombre);

    @Query(value = "SELECT * FROM usuario ORDER BY usuarioId ASC", nativeQuery = true)
    Page<Usuario> findAllOrdered(Pageable pageable);

}
