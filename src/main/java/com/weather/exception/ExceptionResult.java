package com.weather.exception;

import java.time.LocalDateTime;

public class ExceptionResult {
    private String status;
    private String message;
    private String path;
    private LocalDateTime time;

    public ExceptionResult(String status, String message, String path, LocalDateTime time) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
