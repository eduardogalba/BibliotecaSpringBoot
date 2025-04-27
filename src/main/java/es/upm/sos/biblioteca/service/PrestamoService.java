package es.upm.sos.biblioteca.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.upm.sos.biblioteca.exceptions.NoLibrosDisponiblesException;
import es.upm.sos.biblioteca.exceptions.PrestamoNotFoundException;
import es.upm.sos.biblioteca.exceptions.UsuarioPunishmentException;
import es.upm.sos.biblioteca.model.Historico;
import es.upm.sos.biblioteca.model.Libro;
import es.upm.sos.biblioteca.model.Prestamo;
import es.upm.sos.biblioteca.model.PrestamoId;
import es.upm.sos.biblioteca.model.Usuario;
import es.upm.sos.biblioteca.repository.HistoricoRepository;
import es.upm.sos.biblioteca.repository.LibroRepository;
import es.upm.sos.biblioteca.repository.PrestamoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrestamoService {

    private final PrestamoRepository repository;
    private final HistoricoRepository historicoRepository;
    private final LibroRepository libroRepository;

    private Map<Usuario, LocalDateTime> usuariosSancionados = new HashMap<>();

    public List<Prestamo> buscarPorLibroId(int id) {
        return repository.findByLibroLibroId(id);
    }

    public List<Prestamo> buscarPorUsuarioId(int id) {
        return repository.findPrestamosUsuario(id);
    }

    public List<Prestamo> buscarPorUsuarioIdOrdFechPrestamo(int id) {
        return repository.findPrestamosUsuarioPorFechaPrestamo(id);
    }

    public Page<Historico> buscarHistoricoDevuelto(int usuarioId, int page, int size) {
        Pageable paginable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fechaDevolucion"));
        return historicoRepository.findByUsuarioIdAndDevuelto(usuarioId, true, paginable);
    }

    public void prestarLibroAUsuario(PrestamoId geId, Libro libro, Usuario usuario) {
        // Crear la clave primaria compuesta
        geId.setLibroId(libro.getLibroId());

        // Crear la relación
        Prestamo relacion = new Prestamo();
        relacion.setId(geId);
        relacion.setUsuario(usuario);
        relacion.setLibro(libro);

        relacion.setFechaPrestamo(LocalDateTime.now());
        relacion.setFechaDevolucion(LocalDateTime.now().plusDays(14));

        verificarEstadoUsuario(usuario);

        Optional<Integer> librosDisponibles = libroRepository.librosDisponibles();
        if (librosDisponibles.isEmpty() || librosDisponibles.get() == 0) {
            throw new NoLibrosDisponiblesException();
        }

        // Guardar en la base de datos
        repository.save(relacion);
        libroRepository.reservarLibro(libro.getLibroId());

        Historico historico = new Historico();
        historico.setUsuarioId(usuario.getUsuarioId());
        historico.setTituloLibro(libro.getTitulo());
        historico.setIsbnLibro(libro.getIsbn());
        historico.setDevuelto(false);

        // Agregar a la tabla de historico
        historicoRepository.save(historico);
    }

    public Page<Historico> buscarHistoricoUsuario(int id, int page, int size) {
        Pageable paginable = PageRequest.of(page, size);
        return historicoRepository.findHistoricoByUsuarioId(id, paginable);    
    }

    public Optional<Prestamo> buscarPorId(PrestamoId geId) {
        return repository.findById(geId);
    }

    public void devolverLibro(Prestamo prestamo) {
        Usuario usuario = prestamo.getUsuario();
        boolean penalizado = usuariosSancionados.containsKey(usuario);

        if (penalizado) {
            LocalDateTime fechaFinSancion = usuariosSancionados.get(usuario);
            if (LocalDateTime.now().isAfter(fechaFinSancion)) {
                usuariosSancionados.remove(usuario);
                penalizado = false;
            }
        }

        // Evitar más penalizaciones al devolver (devolver es positivo)
        if (fueraPlazo(prestamo.getFechaDevolucion()) && !penalizado) {
            usuariosSancionados.put(usuario, LocalDateTime.now().plusDays(7));
        }

        historicoRepository.devolverHistorico(usuario.getUsuarioId(), prestamo.getLibro().getIsbn());
        repository.deleteById(prestamo.getId());
        libroRepository.devolverLibro(prestamo.getLibro().getLibroId());
    }

    public void ampliarPlazo(PrestamoId geId) {
        Optional<Prestamo> prestamo = repository.findById(geId);
        if (prestamo.isPresent()) {
            verificarEstadoUsuario(prestamo.get().getUsuario());
            historicoRepository.updateFechaDevolucion(geId.getUsuarioId());
            repository.updateFechaDevolucion(geId.getUsuarioId(), geId.getLibroId());
        } else {
            throw new PrestamoNotFoundException(geId.getUsuarioId(), geId.getLibroId());
        }
    }

    private boolean fueraPlazo (LocalDateTime fechaDevolucion) {
        return fechaDevolucion.isBefore(LocalDateTime.now());
    }

    private void verificarEstadoUsuario(Usuario usuario) {
        if (usuariosSancionados.containsKey(usuario)) {
            LocalDateTime fechaFinSancion = usuariosSancionados.get(usuario);
            if (LocalDateTime.now().isBefore(fechaFinSancion)) {
                throw new UsuarioPunishmentException(usuario.getUsuarioId(), fechaFinSancion);
            } else {
                usuariosSancionados.remove(usuario);
            }
        } else {
            List<Prestamo> prestamos = repository.findPrestamosUsuario(usuario.getUsuarioId());
            for (Prestamo prestamo : prestamos) {
                if (fueraPlazo(prestamo.getFechaDevolucion())) {
                    usuariosSancionados.put(usuario, LocalDateTime.now().plusDays(7));
                    throw new UsuarioPunishmentException(usuario.getUsuarioId(), LocalDateTime.now().plusDays(7));
                }
            }
        }
    }

    public Page<Prestamo> buscarPrestamosUsuario (int id, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable paginable = PageRequest.of(page, size);
        if (startDate != null && endDate != null) {
            return repository.findPrestamosByUsuarioIdBetweenFechas(id, startDate, endDate, paginable);
        } else if (startDate != null) {
            return repository.findPrestamosByUsuarioIdAfterFechaPrestamo(id, startDate, paginable);
        } else if (endDate != null) {
            return repository.findPrestamosByUsuarioIdBeforeFechaPrestamo(id, endDate, paginable);
        } else {
            return repository.findPrestamosByUsuarioId(id, paginable);
        }
    }

}
