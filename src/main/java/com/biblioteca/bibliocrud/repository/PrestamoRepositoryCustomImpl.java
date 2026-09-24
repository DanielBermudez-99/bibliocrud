package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Prestamo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

// Implementación de las consultas personalizadas usando EntityManager.
// El nombre debe ser el de la interfaz + "Impl" para que Spring Data la encuentre sola.
public class PrestamoRepositoryCustomImpl implements PrestamoRepositoryCustom {

    // JOIN FETCH trae el usuario y el libro en la misma consulta. Sin él, cada préstamo
    // dispararía consultas extra al leer sus relaciones LAZY (problema N+1).
    private static final String CONSULTA_BASE = """
            SELECT p FROM Prestamo p
            JOIN FETCH p.usuario
            JOIN FETCH p.libro
            """;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Prestamo> buscarTodosConDetalle() {
        return entityManager
                .createQuery(CONSULTA_BASE + "ORDER BY p.fechaPrestamo DESC", Prestamo.class)
                .getResultList();
    }

    @Override
    public List<Prestamo> buscarPorUsuarioConDetalle(Long usuarioId) {
        return entityManager
                .createQuery(CONSULTA_BASE + "WHERE p.usuario.id = :usuarioId ORDER BY p.fechaPrestamo DESC", Prestamo.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }
}
