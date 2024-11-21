package com.intern.app.exception;

import com.intern.app.models.dto.response.ReturnResult;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
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

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ReturnResult<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ReturnResult<String> result = new ReturnResult<>();

        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ReturnResult<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ReturnResult<String> result = new ReturnResult<>();
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;


        result.setCode(errorCode.getCode());
        result.setMessage(enumKey);

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ReturnResult<String>> handlingAuthorizationDeniedException(AuthorizationDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        ReturnResult<String> result = new ReturnResult<>();

        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
