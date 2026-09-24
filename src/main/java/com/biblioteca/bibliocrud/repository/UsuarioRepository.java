package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Nos permite validar si existe el email a la hora de insertar o actualizar
    boolean existsByEmail(String email);
}
