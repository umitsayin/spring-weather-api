package com.weather.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ValidExceptionResult extends ExceptionResult{
    private Map<String, String> errors;

    public ValidExceptionResult(String status, String message, String path, LocalDateTime time) {
        super(status, message, path, time);
        errors = new HashMap<>();
    }

    public void setErrors(BindingResult result){
        for(FieldError e : result.getFieldErrors()){
            errors.put(e.getField(), e.getDefaultMessage());
        }
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
