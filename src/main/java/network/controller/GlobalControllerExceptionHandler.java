package network.controller;

import lombok.extern.slf4j.Slf4j;
import network.dto.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleEntityNotFound(Exception ex) {
        log.error("have problem: ", ex);
        ErrorModel errorModel = new ErrorModel(ex.getMessage(), null, null);
        return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
