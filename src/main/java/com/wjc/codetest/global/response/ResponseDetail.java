package com.wjc.codetest.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDetail<T> {
    private String code;
    private T data;
}
