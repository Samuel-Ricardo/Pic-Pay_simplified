package com.picpay.payment.infra.middleware.error;

import com.picpay.payment.domain.dto.error.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDuplicateEntry(DataIntegrityViolationException exception) {
        exception.printStackTrace();
        var DTO = new ExceptionDTO("User alredy exists", "https://http.cat/status/409");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        "Not Found",
                        "https://http.cat/status/404"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionDTO(
                        exception.getMessage(),
                        "https://http.cat/status/500"
                ));
    }

}
