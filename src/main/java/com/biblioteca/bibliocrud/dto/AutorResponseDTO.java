package com.biblioteca.bibliocrud.dto;

import com.biblioteca.bibliocrud.entity.Autor;

import java.time.LocalDate;

// Especificamos los datos necesarios para insertar un autor

public record AutorResponseDTO(
        Long id,
        String nombre,
        String nacionalidad,
        LocalDate fechaNacimiento
) {

// Desde acá convertimos nuestra clase o entidad en el DTO, obtenemos los valores a devolver
    public static AutorResponseDTO desdeEntidad(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNombre(),
                autor.getNacionalidad(),
                autor.getFechaNacimiento()
        );
    }
}
