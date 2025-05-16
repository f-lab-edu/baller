package com.baller.common.exception;

import org.springframework.http.HttpStatus;

public class ClubNotFoundException  extends BaseException{

    private static final String MESSAGE = "해당 동아리를 찾을 수 없습니다.";

    public ClubNotFoundException(Long clubId) {
        super(MESSAGE + " clubId : " + clubId);
    }

    @Override
    public HttpStatus getStatusCode(){
        return HttpStatus.BAD_REQUEST;
    }

}
