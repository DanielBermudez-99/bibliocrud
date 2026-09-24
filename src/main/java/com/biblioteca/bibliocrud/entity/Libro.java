package com.biblioteca.bibliocrud.entity;

import com.biblioteca.bibliocrud.enums.Genero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    private Integer anioPublicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Genero genero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;
}