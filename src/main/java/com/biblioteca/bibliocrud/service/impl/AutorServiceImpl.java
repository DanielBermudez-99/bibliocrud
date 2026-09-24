package com.biblioteca.bibliocrud.service.impl;

import com.biblioteca.bibliocrud.dto.AutorRequestDTO;
import com.biblioteca.bibliocrud.dto.AutorResponseDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;
import com.biblioteca.bibliocrud.entity.Autor;
import com.biblioteca.bibliocrud.exception.RecursoNoEncontradoException;
import com.biblioteca.bibliocrud.repository.AutorRepository;
import com.biblioteca.bibliocrud.repository.LibroRepository;
import com.biblioteca.bibliocrud.service.AutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Implemento la lógica de negocio de autores
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    @Override
    @Transactional
    public AutorResponseDTO crearAutor(AutorRequestDTO autorDTO) {
        Autor autor = new Autor();
        autor.setNombre(autorDTO.nombre());
        autor.setNacionalidad(autorDTO.nacionalidad());
        autor.setFechaNacimiento(autorDTO.fechaNacimiento());

        Autor guardado = autorRepository.save(autor);
        log.info("Autor creado con id {}", guardado.getId());
        return AutorResponseDTO.desdeEntidad(guardado);
    }

    @Override
    public List<AutorResponseDTO> obtenerTodosLosAutores() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::desdeEntidad)
                .toList();
    }

    @Override
    public AutorResponseDTO obtenerAutorPorId(Long id) {
        return AutorResponseDTO.desdeEntidad(buscarAutor(id));
    }

    @Override
    public List<LibroResponseDTO> obtenerLibrosPorAutor(Long autorId) {
        // Si el autor no existe respondemos 404, no una lista vacía.
        if (!autorRepository.existsById(autorId)) {
            throw new RecursoNoEncontradoException("Autor", autorId);
        }
        return libroRepository.findByAutorId(autorId).stream()
                .map(LibroResponseDTO::desdeEntidad)
                .toList();
    }

    private Autor buscarAutor(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Autor", id));
    }
}
