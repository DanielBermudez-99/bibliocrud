package com.biblioteca.bibliocrud.repository;

import com.biblioteca.bibliocrud.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AutorRepository extends JpaRepository<Autor, Long> {

}