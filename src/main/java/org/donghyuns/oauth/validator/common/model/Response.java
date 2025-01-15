package org.donghyuns.oauth.validator.common.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 포인트 API 서버 연동 공통
 * 응답 규격
 */
@Data
public class Response<T> implements Serializable {

    /** 결과 코드 */
    private String code = "0000";

    /** 결과 메시지 */
    private String message = "성공하였습니다.";

    private int status = HttpStatus.OK.value();

    /** 결과 데이터 */
    private T result;

    /**
     * API 응답 객체 기본 생성자
     */
    public Response() {
    }

    /**
     * API 응답 객체 기본 생성자
     *
     * @param code 결과 코드
     */
    public Response(String code) {
        this.code = code;
        this.result = null;
    }

    /**
     * API 응답 객체 기본 생성자
     *
     * @param code    결과 코드
     * @param message 결과 메사자
     */
    public Response(String code, String message) {
        this.code = code;
        this.message = message;
        this.result = null;
    }

    public Response(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    /**
     * API Result
     *
     * @param result
     */
    public Response(T result) {
        this.result = result;
    }

}
