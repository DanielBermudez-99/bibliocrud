package com.biblioteca.bibliocrud.service;

import com.biblioteca.bibliocrud.dto.LibroRequestDTO;
import com.biblioteca.bibliocrud.dto.LibroResponseDTO;

import java.util.List;

//Especificamos las operaciones a utilizar relacionadas a libros

public interface LibroService {

    LibroResponseDTO crearLibro(LibroRequestDTO libroDTO);

    List<LibroResponseDTO> obtenerTodosLosLibros();

    LibroResponseDTO obtenerLibroPorId(Long id);

    LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libroDTO);

    void eliminarLibro(Long id);
}
