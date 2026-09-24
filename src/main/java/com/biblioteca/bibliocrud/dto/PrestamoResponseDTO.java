package com.biblioteca.bibliocrud.dto;

import com.biblioteca.bibliocrud.entity.Prestamo;

import java.time.LocalDate;

//Especificamos los datos que va a devolver nuestra API prestamos

public record PrestamoResponseDTO(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        Long libroId,
        String libroTitulo,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion,
        boolean devuelto
) {

    // Desde acá convertimos nuestra clase o entidad en el DTO, obtenemos los valores a devolver
    public static PrestamoResponseDTO desdeEntidad(Prestamo prestamo) {
        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getUsuario().getId(),
                prestamo.getUsuario().getNombre(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.isDevuelto()
        );
    }
}
