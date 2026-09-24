package com.biblioteca.bibliocrud.dto;

import com.biblioteca.bibliocrud.entity.Libro;
import com.biblioteca.bibliocrud.enums.Genero;

//Especificamos los datos que va a devolver nuestra API de libros

public record LibroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anioPublicacion,
        Genero genero,
        Long autorId,
        String autorNombre
) {

    // Desde acá convertimos nuestra clase o entidad en el DTO, obtenemos los valores a devolver
    public static LibroResponseDTO desdeEntidad(Libro libro) {
        return new LibroResponseDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getAutor().getId(),
                libro.getAutor().getNombre()
        );
    }
}
