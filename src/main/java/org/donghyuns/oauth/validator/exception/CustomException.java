package org.donghyuns.oauth.validator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

//import org.springframework.http.HttpStatus;
@Slf4j
public class CustomException extends RuntimeException {

    // private Constants.ExceptionClass exceptionClass;
    private ErrorCode errorCode;

    private HttpStatus httpStatus;
    private String focusId;

    public CustomException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
        log.info("CustomException created with errorCode: {} and message: {}", errorCode, errorCode.getMessage());
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        log.info("CustomException created with errorCode: {} and message: {}", errorCode, message);
    }

    public CustomException(String message) {
        super(message);
        log.info("CustomException created with message: {}", message);
    }

    public CustomException(HttpStatus httpStatus, String message, String focusId) {
        super(message);
        this.httpStatus = httpStatus;
        this.focusId = focusId;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
