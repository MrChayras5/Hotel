package com.reservas_commons.controllers;
import com.reservas_commons.exeptions.EntidadRelacionadaException;
import feign.FeignException;
import feign.RetryableException;

import com.reservas_commons.dto.ErrorResponce;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponce> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponce(HttpStatus.BAD_REQUEST.value(), e.getMostSpecificCause().getMessage())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponce> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Violación de restricción: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponce(HttpStatus.BAD_REQUEST.value(), "Violación de restricción: " + e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponce> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación en los datos enviados");
        log.error("Error de validación de argumentos: {}", mensaje);
        return ResponseEntity.badRequest()
                .body(new ErrorResponce(HttpStatus.BAD_REQUEST.value(), mensaje));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponce> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Error en la petición: {}", e.getMessage());
        return ResponseEntity.badRequest().body(
                new ErrorResponce(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponce> handleNoSuchElementException(NoSuchElementException e) {
        log.warn("No se encontró recurso: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponce(HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponce> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("No se encontró el recurso: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponce(HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(EntidadRelacionadaException.class)
    public ResponseEntity<ErrorResponce> handleEntidadRelacionadaException(EntidadRelacionadaException e) {
        log.warn("Error al eliminar un recurso: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponce(HttpStatus.CONFLICT.value(), e.getMessage())
        );
    }
    
    //
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponce> handleGenericFeignException(FeignException e) {
        log.error("Error en la comunicación Feign: " + e.getMessage());

        int status = e.status() > 0 ? e.status() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = switch (status) {
            case 400 -> "Solicitud incorrecta al servicio remoto.";
            case 401 -> "No autorizado para acceder al servicio remoto.";
            case 403 -> "Acceso prohibido al servicio remoto.";
            case 404 -> "Recurso no encontrado en el servicio remoto.";
            case 503 -> "Servicio remoto no disponible.";
            default -> "Error al comunicarse con el servicio remoto.";
        };
        ErrorResponce response = new ErrorResponce(status, message);

        return ResponseEntity.status(status).body(response);
    }
    
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ErrorResponce> handleRetryable(RetryableException e) {
    	log.error("Servicio remoto no disponible o no responde: " + e.getMessage());
    	ErrorResponce response = new ErrorResponce(HttpStatus.SERVICE_UNAVAILABLE.value(),
    			"Servicio remoto no disponible o no respond");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponce> handleException(Exception e) {
        log.error("Error interno del servidor: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponce(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.getMessage() == null ? e.getCause().toString() : e.getMessage())
        );
    }

}
