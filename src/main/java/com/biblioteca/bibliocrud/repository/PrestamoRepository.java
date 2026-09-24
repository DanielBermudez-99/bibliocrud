package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

// Al extender también PrestamoRepositoryCustom, este repositorio suma las consultas
// hechas a mano con EntityManager (ver PrestamoRepositoryCustomImpl).
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>, PrestamoRepositoryCustom {

    //Nos permite si el libro no ha sido devuelto, así no lo prestamos mas de 1 vez
    boolean existsByLibroIdAndDevueltoFalse(Long libroId);

    //Nos permite validar si el libro ha tenido prestamos en su historial, esto para tenerlo en cuenta en el borrado
    boolean existsByLibroId(Long libroId);
}
