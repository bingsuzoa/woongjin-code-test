package com.wjc.codetest.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private ResponseDetail<T> response;

    public static <T> ApiResponse<T> success(ResponseCode code, HttpStatus status, T data) {
        return new ApiResponse<>(
                status.value(),
                code.getMessage(),
                new ResponseDetail<>(code.getCode(), data)
        );
    }

    public static <T> ApiResponse<T> error(ResponseCode code, HttpStatus status) {
        return new ApiResponse<>(
                status.value(),
                code.getMessage(),
                new ResponseDetail<>(code.getCode(), null)
        );
    }
}
