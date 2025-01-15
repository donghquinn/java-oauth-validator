package org.donghyuns.oauth.validator.exception;

public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;
    private Object object;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, Object object) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.object = object;
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(String message) {
        super(message);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getObject() {
        return object;
    }

}
