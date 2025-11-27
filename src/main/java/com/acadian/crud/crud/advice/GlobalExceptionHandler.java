package com.acadian.crud.crud.advice;

import com.acadian.crud.crud.exception.EmployeeNotFoundException;
import com.acadian.crud.crud.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(EmployeeNotFoundException.class)
//    public Mono<Map<String, Object>> handleNotFound(EmployeeNotFoundException ex) {
//
//        return Mono.just(
//                Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "status", HttpStatus.NOT_FOUND.value(),
//                        "error", "Not Found",
//                        "message", ex.getMessage()
//                )
//        );
//    }

//    @ExceptionHandler(Exception.class)
//    public Mono<Map<String, Object>> handleGeneral(Exception ex) {
//        log.error("Unexpected server error occurred", ex);
//        return Mono.just(
//                Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        "error", "Server Error",
//                        "message", ex.getMessage()
//                )
//        );
//    }

    // Improved Error Response Structure with custom error response class
    @ExceptionHandler(EmployeeNotFoundException.class)
    public Mono<ErrorResponse> handleNotFound(EmployeeNotFoundException ex,
                                              ServerWebExchange exchange) {

        return Mono.just(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error("Not Found")
                        .message(ex.getMessage())
                        .path(exchange.getRequest().getPath().value())
                        .requestId(exchange.getRequest().getId())
                        .build()
        );
    }
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleServerErrors(Exception ex,
                                                  ServerWebExchange exchange) {

        log.error("Internal server error: {}", ex.getMessage(), ex);

        return Mono.just(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("Server Error")
                        .message("Internal server error occurred")
                        .path(exchange.getRequest().getPath().value())
                        .requestId(exchange.getRequest().getId())
                        .build()
        );
    }
}
