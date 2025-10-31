package com.wjc.codetest.global.exception;

import com.wjc.codetest.global.response.ApiResponse;
import com.wjc.codetest.global.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(basePackages = "com.wjc.codetest")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("BusinessException: code={}, message={}", e.getResponseCode().getCode(), e.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                e.getResponseCode(),
                e.getStatus()
        );

        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);

        ApiResponse<Void> response = ApiResponse.error(
                ResponseCode.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}