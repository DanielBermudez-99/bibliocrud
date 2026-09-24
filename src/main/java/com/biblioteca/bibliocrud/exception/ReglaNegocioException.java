package com.biblioteca.bibliocrud.exception;


//Se lanza cuando una operación viola una regla del negocio por ejemplo intentar insertar un valor duplicado, un libro que ya prestamos

public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
