package com.biblioteca.bibliocrud.service;

import com.biblioteca.bibliocrud.dto.UsuarioRequestDTO;
import com.biblioteca.bibliocrud.dto.UsuarioResponseDTO;

import java.util.List;

//Especificamos las operaciones a utilizar relacionadas a usuarios

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioDTO);

    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);
}
