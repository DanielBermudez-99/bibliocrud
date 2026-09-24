package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Trae todos los libros junto con su autor en una sola consulta (evita el problema N+1)
    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllConAutor();

    //Nos permite encontrar un libro por medio del id del autor
    List<Libro> findByAutorId(Long autorId);

    // Nos permite validar que el isbn no este repetido al crear un libro
    boolean existsByIsbn(String isbn);

    // Nos permite valirdar que el ISBN no lo use otro libro a la hora de actualizar
    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
