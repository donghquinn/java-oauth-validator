package org.donghyuns.oauth.validator.exception;

import org.donghyuns.oauth.validator.config.WebSecurityConfig;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // File
    FILE_UPLOAD_ERROR(500, "FL001", "error.file.upload.error"),
    FILE_DELETE_NOT_FOUND(500, "FL002", "error.file.delete.not.found"),
    FILE_NOT_FOUND(400, "FL003", "error.file.not.found"),
    FILE_SEQ_UPDATE_ERR(400, "FI004", "error.file.seq.update.err"),

    // User
    USER_NOT_FOUND(400, "US001", "error.member.not.found"),
    USER_SIGNON_ERROR(500,"US004", "error.user.signon.error" ),
    USER_NICKNAME_DUPLICATED(400, "US005", "error.nickname.duplicated"),

    // Oauth
    OAUTH_NOT_FOUND(400, "OA001", "error.oauth.not.found"),

    // User Oauth
    USER_OAUTH_NOT_FOUND(400, "UO001", "error.user.oauth.not.found"),

    // Authorization
    AUTH_FAILURE(HttpStatus.UNAUTHORIZED.value(), "V101", "error.auth.failed"),

    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "F999", "error.system.error"),
    COMMON_EXCEPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C999", "error.common.exception.error"),
    ;

    private static final MessageSourceAccessor messageSourceAccessor;

    static {
        messageSourceAccessor = new MessageSourceAccessor(new WebSecurityConfig().messageSource());
    }

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    // 메시지 값을 기반으로 Enum 값을 반환하는 정적 메서드
    public static ErrorCode fromString(String value) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.name().equalsIgnoreCase(value)) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("Invalid ErrorCode value: " + value);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return messageSourceAccessor.getMessage(message);
    }

    public int getStatus() {
        return status;
    }
}
