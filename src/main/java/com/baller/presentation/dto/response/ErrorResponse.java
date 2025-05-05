package com.baller.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int code;
    private final String message;

    @Builder
    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
