package com.wjc.codetest.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private ResponseDetail<T> response;

    public static <T> ResponseEntity<ApiResponse<T>> success(ResponseCode code, HttpStatus status, T data) {
        ApiResponse<T> body = new ApiResponse<>(
                status.value(),
                code.getMessage(),
                new ResponseDetail<>(code.getCode(), data)
        );
        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(ResponseCode code, T data) {
        return success(code, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ResponseCode code, HttpStatus status) {
        ApiResponse<T> body = new ApiResponse<>(
                status.value(),
                code.getMessage(),
                new ResponseDetail<>(code.getCode(), null)
        );
        return ResponseEntity.status(status).body(body);
    }
}
