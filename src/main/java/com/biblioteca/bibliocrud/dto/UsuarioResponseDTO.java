package com.biblioteca.bibliocrud.dto;

import com.biblioteca.bibliocrud.entity.Usuario;

//Especificamos los datos que va a devolver nuestra API de usuarios
public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String email
) {

    // Desde acá convertimos nuestra clase o entidad en el DTO, obtenemos los valores a devolver
    public static UsuarioResponseDTO desdeEntidad(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }
}
