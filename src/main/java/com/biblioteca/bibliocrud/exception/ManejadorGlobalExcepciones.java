package com.biblioteca.bibliocrud.exception;

import com.biblioteca.bibliocrud.dto.ErrorRespuestaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

//Especificamos el manejo global de excepciones para la aplicacion
@Slf4j
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    // 404: se buscó un registro que no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409: la operación viola una regla del negocio registros duplicados un libro ya prestado
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarReglaNegocio(ReglaNegocioException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return construir(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400: fallo alguna validacion en el dto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(ErrorRespuestaDTO.deValidacion(detalles));
    }

    // 400: error es el cuerpo del json
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        log.warn("Cuerpo de petición inválido: {}", ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es válido o tiene valores con formato incorrecto");
    }

    // 400: un parametro de la api esta mal
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return construir(HttpStatus.BAD_REQUEST, "El parámetro '" + ex.getName() + "' tiene un valor inválido: " + ex.getValue());
    }

    // 409: hubo un error a nivel de base de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos: {}", ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.CONFLICT, "La operación viola una restricción de la base de datos");
    }

    // 500: Conytrolamos algo inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarGeneral(Exception ex) {
        if (ex instanceof ErrorResponse errorSpring) {
            return construir(errorSpring.getStatusCode(), ex.getMessage());
        }
        log.error("Error inesperado", ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno, intente más tarde");
    }

    private ResponseEntity<ErrorRespuestaDTO> construir(HttpStatusCode estado, String mensaje) {
        String nombre = estado instanceof HttpStatus status ? status.getReasonPhrase() : estado.toString();
        return ResponseEntity.status(estado).body(ErrorRespuestaDTO.de(estado.value(), nombre, mensaje));
    }
}
