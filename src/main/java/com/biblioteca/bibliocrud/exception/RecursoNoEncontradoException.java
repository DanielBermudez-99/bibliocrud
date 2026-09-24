package com.biblioteca.bibliocrud.exception;

// Desatamos esta excepcion cuando buscamos un registro que no existe

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con id " + id + " no encontrado");
    }
}