package es.upm.sos.biblioteca.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.upm.sos.biblioteca.model.Usuario;
import es.upm.sos.biblioteca.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service // Marcamos la clase compo componente de servicio
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public boolean existeUsuarioPorId(int id) {
        return repository.existsById(id);
    }

    public boolean existeUsuario(String correo) {
        return repository.existsByCorreo(correo);
    }

    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return repository.findById(id);
    }

    public Page<Usuario> buscarUsuarios(int page, int size) {
        // Crear el objeto Pageable usando el número de página, el tamaño y el campo por
        // el que se ordena (name,desc)
        Pageable paginable = PageRequest.of(page, size);
        return repository.findAllOrdered(paginable);
    }

    public void eliminarUsuario(int id) {
        repository.deleteById(id);
    }

}
