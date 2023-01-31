package com.content_i_like.exception;

import com.content_i_like.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(ContentILikeAppException.class)
  public ResponseEntity<?> SNSAppExceptionHandler(ContentILikeAppException e) {
    Map<String, Object> result = new HashMap<>();
    result.put("errorCode", e.getErrorCode());
    result.put("message", e.getMessage());
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(Response.error("ERROR", result));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
    Map<String, Object> result = new HashMap<>();
    e.getBindingResult().getAllErrors()
        .forEach(c -> result.put(((FieldError) c).getField(), c.getDefaultMessage()));
    return ResponseEntity.status(e.getStatusCode()).body(Response.error("ERROR", result));
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<?> sqlExceptionHandler(SQLException e) {
    Map<String, Object> result = new HashMap<>();
    result.put("errorCode", e.getErrorCode());
    result.put("message", e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Response.error("ERROR", result));
  }
}