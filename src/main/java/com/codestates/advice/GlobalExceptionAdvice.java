package com.codestates.advice;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());

        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(e.getConstraintViolations());

        return response;
    }


    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
//        System.out.println(e.getExceptionCode().getStatus()); // 상태 코드
//        System.out.println(e.getMessage()); // 오류 메시지

        int status = e.getExceptionCode().getStatus();
        String message = e.getExceptionCode().getMessage();

        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());
//        final ErrorResponse response = ErrorResponse.of(status, message);

        // TODO GlobalExceptionAdvice 기능 추가 1
        return new ResponseEntity<>(response, HttpStatus.valueOf(
                e.getExceptionCode().getStatus()));
//        return new ResponseEntity(response, HttpStatus.valueOf(status));
    }

    // TODO GlobalExceptionAdvice 기능 추가 2
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);

        return new ResponseEntity(response, HttpStatus.valueOf(response.getStatus()));
    }

    // TODO GlobalExceptionAdvice 기능 추가 3
    @ExceptionHandler
    public ResponseEntity handleException(NullPointerException e) {
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(response,
                HttpStatus.valueOf(ExceptionCode.INTERNAL_SERVER_ERROR.getStatus()));
    }
}
