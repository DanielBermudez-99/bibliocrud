package com.biblioteca.bibliocrud.dto;

import com.biblioteca.bibliocrud.enums.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// Especificamos los datos necesarios para insertar o actualizar un libro, tambien incluimos validaciones de spring validator

public record LibroRequestDTO(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar 150 caracteres")
        String titulo,

        @NotBlank(message = "El ISBN es obligatorio")
        @Size(max = 20, message = "El ISBN no puede superar 20 caracteres")
        String isbn,

        @NotNull(message = "El año de publicación es obligatorio")
        @Positive(message = "El año de publicación debe ser positivo")
        Integer anioPublicacion,

        @NotNull(message = "El género es obligatorio")
        Genero genero,

        @NotNull(message = "El autor es obligatorio")
        Long autorId
) {
}
