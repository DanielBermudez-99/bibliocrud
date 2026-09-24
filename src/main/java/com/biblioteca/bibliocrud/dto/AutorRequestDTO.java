package com.biblioteca.bibliocrud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

//Desde acá especificamos los datos necesarios para crear un autor

public record AutorRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Size(max = 60, message = "La nacionalidad no puede superar 60 caracteres")
        String nacionalidad,

        @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
        LocalDate fechaNacimiento
) {
}
