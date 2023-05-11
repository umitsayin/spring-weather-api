package com.weather.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResult validExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidExceptionResult result = new ValidExceptionResult(HttpStatus.BAD_REQUEST.toString(),
                "VALIDATION_EXCEPTION",
                request.getServletPath(),
                LocalDateTime.now());

        result.setErrors(e.getBindingResult());

        return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionResult validExceptionHandler(ConstraintViolationException e, HttpServletRequest request){
        ExceptionResult result = new ExceptionResult(HttpStatus.BAD_REQUEST.toString(),
                e.getMessage(),
                request.getServletPath(),
                LocalDateTime.now());


        return result;
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ExceptionResult> toManyRequestException(RequestNotPermitted e, HttpServletRequest request){
        ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.TOO_MANY_REQUESTS.toString(),
                e.getMessage(),
                request.getServletPath(),
                LocalDateTime.now());

        return ResponseEntity.ok().body(exceptionResult);
    }
}
