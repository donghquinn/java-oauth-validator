package org.donghyuns.oauth.validator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 1. 에러코드 정리 enum 클래스로 작성
     * 2. Exception 발생시 응답하는 에러 정보 클래스 작성
     * 3. 사용자 정의 Exception 클래스 작성
     * 4. Exception 발생시 전역으로 처리할 exception handler 작성
     * 5. 사용자등록관련 클래스작성 서비스에서 중복 exception 발생
     * 6. api 실행 및 exception 결과 확인
     */

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        logger.error("handleHttpRequestMethodNotSupportedException", e);

        final ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // @Valid 검증 실패 시 Catch. 1차 필터링이므로 무조건 500 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException", e);
        // 유효성 검사 실패에 대한 메시지 추출
        String errorString = e.getBindingResult().getFieldError().getDefaultMessage();
        // ErrorCode errorCode = ErrorCode.fromString(errorString);
        ErrorResponse response = ErrorResponse
                .create()
                .status(500)
                .code("INV999")
                .message(errorString);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // CustomException을 상속받은 클래스가 예외를 발생 시킬 시, Catch하여 ErrorResponse를 반환한다.
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        logger.error("handleAllException", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = null;
        if (errorCode != null) {
            response = ErrorResponse
                    .create()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage());
        } else {
            response = ErrorResponse
                    .create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .code(ErrorCode.COMMON_EXCEPTION_ERROR.getCode())
                    .message(e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.resolve(response.getStatus()));
    }

    // 모든 예외를 핸들링하여 ErrorResponse 형식으로 반환한다.
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("handleException", e);

        ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * system handler
     *
     * @param e
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<SystemResponse> handleSystemException(SystemException e) {
        logger.error("handleSystemException", e);
        ErrorCode errorCode = e.getErrorCode();
        SystemResponse<Object> systemResponse = null;
        if (errorCode != null) {
            systemResponse = SystemResponse
                    .create()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .object(e.getObject());
        } else {
            systemResponse = SystemResponse
                    .create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .code(ErrorCode.COMMON_EXCEPTION_ERROR.getCode())
                    .message(e.getMessage());
        }

        return new ResponseEntity<>(systemResponse, HttpStatus.resolve(systemResponse.getStatus()));
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        String errorMessage = "Invalid JSON format: " + ex.getMessage();
//        // 로그로 추가 디버깅 정보 기록
//        ex.printStackTrace();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid JSON: " + ex.getMessage());
    }

}
