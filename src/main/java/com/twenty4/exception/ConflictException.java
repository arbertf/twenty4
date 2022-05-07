package com.twenty4.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ConflictException extends RuntimeException{

    private final ErrorCode errorCode;
    private final HttpStatus statusCode;
    private List<String> parameters;

    public ConflictException(ErrorCode errorCode, HttpStatus httpStatusCode) {
        super(errorCode.getMessageTitleKey());
        this.errorCode = errorCode;
        this.statusCode = httpStatusCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
    /**
     * Return parameters as Object[] as MessageSource requires
     */
    public Object[] getParameters() {
        return this.parameters == null ? null : this.parameters.toArray();
    }
}
