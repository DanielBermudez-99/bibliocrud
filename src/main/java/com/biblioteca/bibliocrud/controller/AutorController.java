package com.biblioteca.bibliocrud.controller;

import com.biblioteca.bibliocrud.dto.AutorRequestDTO;
import com.biblioteca.bibliocrud.dto.AutorResponseDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;
import com.biblioteca.bibliocrud.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Especifico que voy a trabajar Endpoints de tipo rest
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    // Metodo post para crear autores
    @PostMapping
    public ResponseEntity<AutorResponseDTO> crearAutor(@Valid @RequestBody AutorRequestDTO autorDTO) {
        AutorResponseDTO autor = autorService.crearAutor(autorDTO);
        return new ResponseEntity<>(autor, HttpStatus.CREATED);
    }

    // Metodo get para obtener todos los autores
    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> obtenerTodosLosAutores() {
        return ResponseEntity.ok(autorService.obtenerTodosLosAutores());
    }

    // Metodo get para obtener autor por el id
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> obtenerAutorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.obtenerAutorPorId(id));
    }

    // Metodo get para obtener los libros del autor
    @GetMapping("/{id}/libros")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibrosPorAutor(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.obtenerLibrosPorAutor(id));
    }
}
