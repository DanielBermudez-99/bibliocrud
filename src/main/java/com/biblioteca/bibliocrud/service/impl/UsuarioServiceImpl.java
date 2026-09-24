package com.biblioteca.bibliocrud.service.impl;

import com.biblioteca.bibliocrud.dto.UsuarioRequestDTO;
import com.biblioteca.bibliocrud.dto.UsuarioResponseDTO;
import com.biblioteca.bibliocrud.entity.Usuario;
import com.biblioteca.bibliocrud.exception.RecursoNoEncontradoException;
import com.biblioteca.bibliocrud.exception.ReglaNegocioException;
import com.biblioteca.bibliocrud.repository.UsuarioRepository;
import com.biblioteca.bibliocrud.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Implemento la lógica de negocio de usuarios
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioDTO) {
        // Regla: no puede haber dos usuarios con el mismo email
        if (usuarioRepository.existsByEmail(usuarioDTO.email())) {
            throw new ReglaNegocioException("Ya existe un usuario con email " + usuarioDTO.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setEmail(usuarioDTO.email());

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con id {}", guardado.getId());
        return UsuarioResponseDTO.desdeEntidad(guardado);
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::desdeEntidad)
                .toList();
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
        return UsuarioResponseDTO.desdeEntidad(usuario);
    }
}
