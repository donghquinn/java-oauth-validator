package org.donghyuns.oauth.validator.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * 포인트 API 서버 연동 공통
 * 응답 규격
 */
@Data
public class SystemResponse<T> implements Serializable {
    private String message; // 예외 메시지 저장

    private String code; // 예외를 세분화하기 위한 사용자 지정 코드,

    private int status; // HTTP 상태 값 저장 400, 404, 500 등..

    private T object;

    public SystemResponse() {
    }

    @SuppressWarnings("rawtypes")
    static public SystemResponse create() {
        return new SystemResponse();
    }

    @SuppressWarnings("rawtypes")
    public SystemResponse code(String code) {
        this.code = code;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SystemResponse status(int status) {
        this.status = status;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SystemResponse message(String message) {
        this.message = message;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SystemResponse object(T object) {
        this.object = object;
        return this;
    }

}
