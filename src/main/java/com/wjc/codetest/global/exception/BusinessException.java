package com.wjc.codetest.global.exception;

import com.wjc.codetest.global.response.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseCode responseCode;
    private final HttpStatus status;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.status = HttpStatus.BAD_REQUEST;
    }
}