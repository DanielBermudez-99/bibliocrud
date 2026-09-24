package com.biblioteca.bibliocrud.dto;

import java.time.LocalDateTime;
import java.util.Map;

// Formato único con el que la API responde cualquier error.
// "detalles" solo se llena en errores de validación: campo -> mensaje.
public record ErrorRespuestaDTO(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        Map<String, String> detalles
) {

    public static ErrorRespuestaDTO de(int estado, String error, String mensaje) {
        return new ErrorRespuestaDTO(LocalDateTime.now(), estado, error, mensaje, Map.of());
    }

    public static ErrorRespuestaDTO deValidacion(Map<String, String> detalles) {
        return new ErrorRespuestaDTO(LocalDateTime.now(), 400, "Bad Request", "Datos de entrada inválidos", detalles);
    }
}
