package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Prestamo;

import java.util.List;

// Consultas personalizadas de préstamos que implementamos a mano con EntityManager.
// Spring Data las "pega" a PrestamoRepository porque este extiende esta interfaz.
public interface PrestamoRepositoryCustom {

    // Todos los préstamos con su usuario y su libro cargados en una sola consulta
    List<Prestamo> buscarTodosConDetalle();

    // Préstamos de un usuario con su usuario y su libro cargados en una sola consulta
    List<Prestamo> buscarPorUsuarioConDetalle(Long usuarioId);
}
