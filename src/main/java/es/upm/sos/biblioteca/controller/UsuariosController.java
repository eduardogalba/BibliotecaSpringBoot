package es.upm.sos.biblioteca.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.upm.sos.biblioteca.assembler.UsuarioModelAssembler;
import es.upm.sos.biblioteca.exceptions.LibroNotFoundException;
import es.upm.sos.biblioteca.exceptions.PrestamoNotFoundException;
import es.upm.sos.biblioteca.exceptions.UsuarioExistsException;
import es.upm.sos.biblioteca.exceptions.UsuarioHasPrestamosException;
import es.upm.sos.biblioteca.exceptions.UsuarioNotFoundException;
import es.upm.sos.biblioteca.model.Historico;
import es.upm.sos.biblioteca.model.Libro;
import es.upm.sos.biblioteca.model.Prestamo;
import es.upm.sos.biblioteca.model.PrestamoId;
import es.upm.sos.biblioteca.model.Usuario;
import es.upm.sos.biblioteca.service.LibroService;
import es.upm.sos.biblioteca.service.PrestamoService;
import es.upm.sos.biblioteca.service.UsuarioService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;

@RestController // Define un controlador REST
@RequestMapping("/usuarios") // Todos los endpoints empiezan por usuarios
@XmlRootElement // Para indicar que soportamos xml (opcional)
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class UsuariosController {

    // Acceso al servicio usuario para acceder a la lógica de negocio
    private final UsuarioService usuarioService;
    private final PrestamoService prestamoService;
    private final LibroService libroService;
    // Necesarios para navegabiliad
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private PagedResourcesAssembler<Prestamo> prestamoPagedResourcesAssembler;
    private PagedResourcesAssembler<Historico> historicoPagedResourcesAssembler;
    private UsuarioModelAssembler usuarioModelAssembler;

    @GetMapping(value = "", produces = { "application/json" })
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
            @RequestParam(defaultValue = "0", required = true) int page,
            @RequestParam(defaultValue = "2", required = true) int size) {

        Page<Usuario> usuarios = usuarioService.buscarUsuarios(page, size);

        // fetch the page object by additionally passing paginable with the filters
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler));
    }

    // Operación POST usuario
    @PostMapping(value = "", consumes = { "application/json" })
    public ResponseEntity<Void> nuevoUsuario(@Valid @RequestBody Usuario nuevoUsuario) {
        // Comprobamos si existe el usuario
        if (!usuarioService.existeUsuario(nuevoUsuario.getCorreo())) {
            // Si no existe lo guardamos en la base de datos
            Usuario usuario = usuarioService.crearUsuario(nuevoUsuario);
            // Añadimos la cabecera Location con la referencia el nuevo recurso
            return ResponseEntity.created(linkTo(UsuariosController.class).slash(usuario.getUsuarioId()).toUri())
                    .build();
        }
        // Si existe lanzamos una excepción que se captura en la clase
        // UsuarioExceptionAdvice y envía la respuesta con el código 409 - CONFLICT
        throw new UsuarioExistsException(nuevoUsuario.getNombre());

    }

    @GetMapping(value = "/{id}", produces = { "application/json" })
    public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.add(linkTo(methodOn(UsuariosController.class).getUsuario(id)).withSelfRel());
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> replaceUsuario(@Valid @RequestBody Usuario newUsuario, @PathVariable Integer id) {
        usuarioService.buscarPorId(id)
                .map(usuario -> {
                    usuario.setNombre(newUsuario.getNombre());
                    usuario.setMatricula(newUsuario.getMatricula());
                    usuario.setNacimiento(newUsuario.getNacimiento());
                    usuario.setCorreo(newUsuario.getCorreo());
                    return usuarioService.crearUsuario(usuario);
                })
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (usuarioService.existeUsuarioPorId(id)) {
            try {
                usuarioService.eliminarUsuario(id);
            } catch (DataIntegrityViolationException e) {
                throw new UsuarioHasPrestamosException(id);
            }

        } else {
            throw new UsuarioNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{usuarioId}/libros/{libroId}")
    public ResponseEntity<Prestamo> getUsuarioLibroPrestamo(@PathVariable Integer usuarioId,
            @PathVariable Integer libroId) {
        PrestamoId prestamoId = new PrestamoId(usuarioId, libroId);
        Prestamo prestamo = prestamoService.buscarPorId(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException(usuarioId, libroId));
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping(value = "/{id}/libros", consumes = { "application/json" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> addLibroToUsuario(@PathVariable Integer id,
            @RequestBody PrestamoId nuevoPrestamo) {
        // Buscar libro y usuario en la base de datos
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        Libro libro = libroService.buscarPorId(nuevoPrestamo.getLibroId())
                .orElseThrow(() -> new LibroNotFoundException(
                        nuevoPrestamo.getLibroId()));
        prestamoService.prestarLibroAUsuario(nuevoPrestamo, libro, usuario);
        return ResponseEntity.created(linkTo(UsuariosController.class).slash(id).slash("usuarios")
                .slash(libro.getLibroId()).toUri()).build();
    }

    @PutMapping("/{id}/libros/{libroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateLibroToUsuario(@PathVariable Integer id, @PathVariable Integer libroId) {
        if (!usuarioService.existeUsuarioPorId(id)) {
            throw new LibroNotFoundException(id);
        } else if (!libroService.existeLibroPorId(libroId)) {
            throw new UsuarioNotFoundException(libroId);
        }

        PrestamoId presId = new PrestamoId();
        presId.setLibroId(id);
        presId.setUsuarioId(libroId);
        prestamoService.buscarPorId(presId)
                .orElseThrow(() -> new PrestamoNotFoundException(id, libroId));
        prestamoService.ampliarPlazo(presId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/libros/{libroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeLibroToUsuario(@PathVariable Integer id, @PathVariable Integer libroId) {
        if (!usuarioService.existeUsuarioPorId(id)) {
            throw new LibroNotFoundException(id);
        } else if (!libroService.existeLibroPorId(libroId)) {
            throw new UsuarioNotFoundException(libroId);
        }

        PrestamoId presId = new PrestamoId();
        presId.setLibroId(id);
        presId.setUsuarioId(libroId);
        Prestamo prestamo = prestamoService.buscarPorId(presId)
                .orElseThrow(() -> new PrestamoNotFoundException(id, libroId));
        prestamoService.devolverLibro(prestamo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/actividad", produces = { "application/json" })
    public ResponseEntity<Usuario> getActividadUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        Set<EntityModel<Libro>> listaPrestamos = new HashSet<>();
        for (Prestamo prestamo : prestamoService.buscarPorUsuarioIdOrdFechPrestamo(id)) {
            listaPrestamos.add(EntityModel.of(prestamo.getLibro(),
                    linkTo(methodOn(LibrosController.class)
                            .getLibro(prestamo.getLibro().getLibroId()))
                            .withSelfRel()));
        }
        usuario.setPrestamos(listaPrestamos);
        Set<EntityModel<Historico>> listaDevueltos = new HashSet<>();
        prestamoService.buscarHistoricoDevuelto(id, 0, 5).forEach(historico -> {
            EntityModel<Historico> historicoModel = EntityModel.of(historico);
            listaDevueltos.add(historicoModel);
        });
        usuario.setDevueltos(listaDevueltos);
        usuario.add(linkTo(methodOn(UsuariosController.class).getUsuario(id)).withSelfRel());
        return ResponseEntity.ok(usuario);
    }

    @GetMapping(value = "/{id}/historico", produces = { "application/json" })
    public ResponseEntity<PagedModel<EntityModel<Historico>>> getHistoricoUsuario(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0", required = true) int page,
            @RequestParam(defaultValue = "2", required = true) int size) {
        if (!usuarioService.existeUsuarioPorId(id)) {
            throw new UsuarioNotFoundException(id);
        }
        Page<Historico> historicoPage = prestamoService.buscarHistoricoUsuario(id, page, size);
        PagedModel<EntityModel<Historico>> pagedModel = historicoPagedResourcesAssembler.toModel(historicoPage,
                EntityModel::of);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping(value = "/{id}/prestamos", produces = { "application/json" })
    public ResponseEntity<PagedModel<EntityModel<Prestamo>>> getPrestamosUsuario(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "", required = false) String startDate,
            @RequestParam(defaultValue = "", required = false) String endDate,
            @RequestParam(defaultValue = "0", required = true) int page,
            @RequestParam(defaultValue = "2", required = true) int size) {

        LocalDateTime startDateDT = parseDate(startDate);
        LocalDateTime endDateDT = parseDate(endDate);

        Page<Prestamo> prestamosPage = prestamoService.buscarPrestamosUsuario(id, startDateDT, endDateDT, page, size);
        Set<Usuario> usuariosProcesados = new HashSet<>();
        for (Prestamo prestamo : prestamosPage) {
            prestamo.getLibro().add(linkTo(methodOn(LibrosController.class)
                    .getLibro(prestamo.getLibro().getLibroId()))
                    .withSelfRel());
            prestamo.add(linkTo(methodOn(UsuariosController.class)
                    .getUsuarioLibroPrestamo(prestamo.getUsuario().getUsuarioId(), prestamo.getLibro().getLibroId()))
                    .withSelfRel());
            Usuario usuario = prestamo.getUsuario();
            if (!usuariosProcesados.contains(usuario)) {
                usuario.add(linkTo(methodOn(UsuariosController.class)
                        .getUsuario(usuario.getUsuarioId()))
                        .withSelfRel());
                usuariosProcesados.add(usuario);
            }
        }

        PagedModel<EntityModel<Prestamo>> pagedModel = prestamoPagedResourcesAssembler.toModel(prestamosPage,
                EntityModel::of);

        return ResponseEntity.ok(pagedModel);
    }

    private LocalDateTime parseDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatterWithoutTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;

        try {
            return LocalDateTime.parse(date, formatterWithTime);
        } catch (DateTimeParseException e) {

        }

        try {
            return LocalDate.parse(date, formatterWithoutTime).atStartOfDay();
        } catch (DateTimeParseException e) {

        }

        try {
            return LocalDateTime.parse(date, isoFormatter);
        } catch (DateTimeParseException e) {

        }
        
        throw new IllegalArgumentException(
                "El formato de la fecha debe ser uno de los siguientes: dd/MM/yyyy, dd/MM/yyyy HH:mm:ss, o ISO_DATE_TIME (yyyy-MM-dd'T'HH:mm:ss)");
    }

}
