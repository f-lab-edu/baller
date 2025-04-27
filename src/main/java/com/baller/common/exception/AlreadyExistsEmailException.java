package com.baller.common.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsEmailException extends BaseException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode(){
        return HttpStatus.BAD_REQUEST;
    }

}
