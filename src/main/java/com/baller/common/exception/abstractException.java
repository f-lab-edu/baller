package com.baller.common.exception;

import lombok.Getter;

@Getter
public abstract class abstractException extends RuntimeException {

    public abstractException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
