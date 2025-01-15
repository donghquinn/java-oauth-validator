package org.donghyuns.oauth.validator.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    // private Instant timestamp = Instant.now();

    private String message; // 예외 메시지 저장

    private String code; // 예외를 세분화하기 위한 사용자 지정 코드,

    private int status; // HTTP 상태 값 저장 400, 404, 500 등..

    public ErrorResponse() {
    }

    static public ErrorResponse create() {
        return new ErrorResponse();
    }

    public ErrorResponse code(String code) {
        this.code = code;
        return this;
    }

    public ErrorResponse status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

}
