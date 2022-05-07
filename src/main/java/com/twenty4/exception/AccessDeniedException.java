package com.twenty4.exception;

public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
