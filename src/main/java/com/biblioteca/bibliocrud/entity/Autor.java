package com.biblioteca.bibliocrud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 60)
    private String nacionalidad;

    private LocalDate fechaNacimiento;

    @OneToMany(mappedBy = "autor")
    private List<Libro> libros = new ArrayList<>();
}