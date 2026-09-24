package com.biblioteca.bibliocrud.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Especificamos los datos necesarios para registrar un prestamo

public record PrestamoRequestDTO(

        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El libro es obligatorio")
        Long libroId,

        @NotNull(message = "La fecha de préstamo es obligatoria")
        LocalDate fechaPrestamo,

        @NotNull(message = "La fecha de devolución es obligatoria")
        LocalDate fechaDevolucion
) {
}
