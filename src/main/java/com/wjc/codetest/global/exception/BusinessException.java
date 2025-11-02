package com.wjc.codetest.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String detailMessage;

    public BusinessException(ErrorCode code) {
        super(code.getErrorCode());
        statusCode = code.getStatusCode();
        errorCode = code.getErrorCode();
        this.detailMessage = code.getMessage();
    }
}