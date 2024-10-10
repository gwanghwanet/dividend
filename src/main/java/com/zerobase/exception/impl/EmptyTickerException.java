package com.zerobase.exception.impl;

import com.zerobase.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmptyTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "종목 코드(Ticker)가 입력되지 않았습니다.";
    }
}
