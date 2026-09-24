package com.biblioteca.bibliocrud.service.impl;

import com.biblioteca.bibliocrud.dto.LibroRequestDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;
import com.biblioteca.bibliocrud.entity.Autor;
import com.biblioteca.bibliocrud.entity.Libro;
import com.biblioteca.bibliocrud.exception.RecursoNoEncontradoException;
import com.biblioteca.bibliocrud.exception.ReglaNegocioException;
import com.biblioteca.bibliocrud.repository.AutorRepository;
import com.biblioteca.bibliocrud.repository.LibroRepository;
import com.biblioteca.bibliocrud.repository.PrestamoRepository;
import com.biblioteca.bibliocrud.service.LibroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

//Implemento la lógica de negocio de para libros
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final PrestamoRepository prestamoRepository;

    @Override
    @Transactional
    public LibroResponseDTO crearLibro(LibroRequestDTO libroDTO) {
        if (libroRepository.existsByIsbn(libroDTO.isbn())) {
            throw new ReglaNegocioException("Ya existe un libro con ISBN " + libroDTO.isbn());
        }

        Libro libro = new Libro();
        aplicarDatos(libro, libroDTO);

        Libro guardado = libroRepository.save(libro);
        log.info("Libro creado con id {} e ISBN {}", guardado.getId(), guardado.getIsbn());
        return LibroResponseDTO.desdeEntidad(guardado);
    }

    @Override
    public List<LibroResponseDTO> obtenerTodosLosLibros() {
        return libroRepository.findAllConAutor().stream()
                .map(LibroResponseDTO::desdeEntidad)
                .toList();
    }

    @Override
    public LibroResponseDTO obtenerLibroPorId(Long id) {
        return LibroResponseDTO.desdeEntidad(buscarLibro(id));
    }

    @Override
    @Transactional
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libroDTO) {
        Libro libro = buscarLibro(id);

        if (libroRepository.existsByIsbnAndIdNot(libroDTO.isbn(), id)) {
            throw new ReglaNegocioException("Ya existe otro libro con ISBN " + libroDTO.isbn());
        }

        aplicarDatos(libro, libroDTO);
        // No hace falta save(): la entidad está gestionada por JPA y los cambios
        // se guardan solos al terminar la transacción (dirty checking).
        log.info("Libro {} actualizado", id);
        return LibroResponseDTO.desdeEntidad(libro);
    }

    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        Libro libro = buscarLibro(id);

        if (prestamoRepository.existsByLibroId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el libro " + id + " porque tiene préstamos registrados");
        }

        libroRepository.delete(libro);
        log.info("Libro {} eliminado", id);
    }

    /** Copia los datos del DTO a la entidad, validando año y autor. */
    private void aplicarDatos(Libro libro, LibroRequestDTO libroDTO) {
        if (libroDTO.anioPublicacion() > Year.now().getValue()) {
            throw new ReglaNegocioException("El año de publicación no puede ser futuro");
        }

        Autor autor = autorRepository.findById(libroDTO.autorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Autor", libroDTO.autorId()));

        libro.setTitulo(libroDTO.titulo());
        libro.setIsbn(libroDTO.isbn());
        libro.setAnioPublicacion(libroDTO.anioPublicacion());
        libro.setGenero(libroDTO.genero());
        libro.setAutor(autor);
    }

    private Libro buscarLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro", id));
    }
}
