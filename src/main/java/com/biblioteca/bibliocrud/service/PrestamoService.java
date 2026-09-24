package com.biblioteca.bibliocrud.service;

import com.biblioteca.bibliocrud.dto.PrestamoRequestDTO;
import com.biblioteca.bibliocrud.dto.PrestamoResponseDTO;

import java.util.List;

//Especificamos las operaciones a utilizar relacionadas a préstamos

public interface PrestamoService {

    PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO prestamoDTO);

    List<PrestamoResponseDTO> obtenerTodosLosPrestamos();

    List<PrestamoResponseDTO> obtenerPrestamosPorUsuario(Long usuarioId);

    PrestamoResponseDTO marcarComoDevuelto(Long id);
}
