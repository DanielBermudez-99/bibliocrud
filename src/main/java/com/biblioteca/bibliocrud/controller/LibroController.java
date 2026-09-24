package com.biblioteca.bibliocrud.controller;

import com.biblioteca.bibliocrud.dto.LibroRequestDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;
import com.biblioteca.bibliocrud.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Especifico que voy a trabajar Endpoints de tipo rest
@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    // Metodo post para crear libros
    @PostMapping
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroRequestDTO libroDTO) {
        LibroResponseDTO libro = libroService.crearLibro(libroDTO);
        return new ResponseEntity<>(libro, HttpStatus.CREATED);
    }

    // Metodo get para obtener todos los libros
    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> obtenerTodosLosLibros() {
        return ResponseEntity.ok(libroService.obtenerTodosLosLibros());
    }

    // Metodo get para obtener libro por id
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerLibroPorId(id));
    }

    // Metodo put para actualizar un libro
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable Long id,
                                                            @Valid @RequestBody LibroRequestDTO libroDTO) {
        return ResponseEntity.ok(libroService.actualizarLibro(id, libroDTO));
    }

    // Metodo de borrado para borrar un libro por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}
