package com.biblioteca.bibliocrud.controller;

import com.biblioteca.bibliocrud.dto.PrestamoRequestDTO;
import com.biblioteca.bibliocrud.dto.PrestamoResponseDTO;
import com.biblioteca.bibliocrud.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    // Metodo post para registrar un prestamo
    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrarPrestamo(@Valid @RequestBody PrestamoRequestDTO prestamoDTO) {
        PrestamoResponseDTO prestamo = prestamoService.registrarPrestamo(prestamoDTO);
        return new ResponseEntity<>(prestamo, HttpStatus.CREATED);
    }

    // Metodo get para consultar todos los prestamos
    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerTodosLosPrestamos() {
        return ResponseEntity.ok(prestamoService.obtenerTodosLosPrestamos());
    }

    // Metodo get para consultar prestamos de usuario por id
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamosPorUsuario(usuarioId));
    }

    //Metodo put para actualizar el estado de devuelto
    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> marcarComoDevuelto(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.marcarComoDevuelto(id));
    }
}
