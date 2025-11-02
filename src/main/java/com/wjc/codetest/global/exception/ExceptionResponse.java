package com.wjc.codetest.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private final String message;

    public static ExceptionResponse from(String e) {
        return new ExceptionResponse(e);
    }
}
