package com.wjc.codetest.global.response;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriBuilder {

    public static URI buildCurrentUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
