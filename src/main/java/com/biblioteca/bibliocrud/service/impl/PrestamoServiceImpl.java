package com.biblioteca.bibliocrud.service.impl;

import com.biblioteca.bibliocrud.dto.PrestamoRequestDTO;
import com.biblioteca.bibliocrud.dto.PrestamoResponseDTO;
import com.biblioteca.bibliocrud.entity.Libro;
import com.biblioteca.bibliocrud.entity.Prestamo;
import com.biblioteca.bibliocrud.entity.Usuario;
import com.biblioteca.bibliocrud.exception.RecursoNoEncontradoException;
import com.biblioteca.bibliocrud.exception.ReglaNegocioException;
import com.biblioteca.bibliocrud.repository.LibroRepository;
import com.biblioteca.bibliocrud.repository.PrestamoRepository;
import com.biblioteca.bibliocrud.repository.UsuarioRepository;
import com.biblioteca.bibliocrud.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Implemento la lógica de negocio de préstamos
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Override
    @Transactional
    public PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO prestamoDTO) {
        // Regla: la devolución debe ser posterior al préstamo
        if (!prestamoDTO.fechaDevolucion().isAfter(prestamoDTO.fechaPrestamo())) {
            throw new ReglaNegocioException("La fecha de devolución debe ser posterior a la fecha de préstamo");
        }

        Usuario usuario = usuarioRepository.findById(prestamoDTO.usuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", prestamoDTO.usuarioId()));

        Libro libro = libroRepository.findById(prestamoDTO.libroId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro", prestamoDTO.libroId()));

        // Regla: un libro no se puede prestar si tiene un préstamo sin devolver
        if (prestamoRepository.existsByLibroIdAndDevueltoFalse(libro.getId())) {
            throw new ReglaNegocioException("El libro " + libro.getId() + " ya se encuentra prestado");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(prestamoDTO.fechaPrestamo());
        prestamo.setFechaDevolucion(prestamoDTO.fechaDevolucion());

        Prestamo guardado = prestamoRepository.save(prestamo);
        log.info("Préstamo {} registrado: libro {} al usuario {}", guardado.getId(), libro.getId(), usuario.getId());
        return PrestamoResponseDTO.desdeEntidad(guardado);
    }

    @Override
    public List<PrestamoResponseDTO> obtenerTodosLosPrestamos() {
        return prestamoRepository.buscarTodosConDetalle().stream()
                .map(PrestamoResponseDTO::desdeEntidad)
                .toList();
    }

    @Override
    public List<PrestamoResponseDTO> obtenerPrestamosPorUsuario(Long usuarioId) {
        // Si el usuario no existe respondemos 404, no una lista vacía.
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario", usuarioId);
        }
        return prestamoRepository.buscarPorUsuarioConDetalle(usuarioId).stream()
                .map(PrestamoResponseDTO::desdeEntidad)
                .toList();
    }

    @Override
    @Transactional
    public PrestamoResponseDTO marcarComoDevuelto(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo", id));

        // Regla: un préstamo no se puede devolver dos veces
        if (prestamo.isDevuelto()) {
            throw new ReglaNegocioException("El préstamo " + id + " ya fue devuelto");
        }

        prestamo.setDevuelto(true);
        // Sin save(): dirty checking guarda el cambio al terminar la transacción.
        log.info("Préstamo {} marcado como devuelto", id);
        return PrestamoResponseDTO.desdeEntidad(prestamo);
    }
}
