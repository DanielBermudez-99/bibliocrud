package com.biblioteca.bibliocrud.service;

import com.biblioteca.bibliocrud.dto.AutorRequestDTO;
import com.biblioteca.bibliocrud.dto.AutorResponseDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;

import java.util.List;

//Especificamos las operaciones a utilizar relacionadas a autor

public interface AutorService {

    // Tenemos el metodo crear autor, usamos el dto de request para utilizar los datos necesarios
    AutorResponseDTO crearAutor(AutorRequestDTO autorDTO);

    //Obtenemos los autores en una lista por medio de nuestro DTO de respuesta
    List<AutorResponseDTO> obtenerTodosLosAutores();

    AutorResponseDTO obtenerAutorPorId(Long id);

    List<LibroResponseDTO> obtenerLibrosPorAutor(Long autorId);
}
