package com.intern.app.exception;

import com.intern.app.dto.response.ReturnResult;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ReturnResult<String>> handlingRuntimeException(RuntimeException e) {
        ReturnResult<String> result = new ReturnResult<>();

        result.setCode(Response.SC_BAD_REQUEST);
        result.setMessage(e.getMessage());
        result.setResult(e.getMessage());

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ReturnResult<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ReturnResult<String> result = new ReturnResult<>();

        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ReturnResult<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ReturnResult<String> result = new ReturnResult<>();
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException illegalArgumentException){

        }

        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        result.setResult(errorCode.getMessage());

        return ResponseEntity.badRequest().body(result);
    }
}
