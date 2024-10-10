package com.zerobase.exception.impl;

import com.zerobase.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FailedScrapTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return "해당 종목 코드를 스크래핑하는 데 실패하였습니다.";
    }
}
