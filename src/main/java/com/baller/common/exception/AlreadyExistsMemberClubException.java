package com.baller.common.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsMemberClubException extends BaseException {

    private static final String MESSAGE = "이미 해당 동아리에 가입한 회원입니다.";

    public AlreadyExistsMemberClubException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode(){
        return HttpStatus.BAD_REQUEST;
    }

}
