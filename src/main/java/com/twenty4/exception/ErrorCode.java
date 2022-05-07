package com.twenty4.exception;

public enum ErrorCode {

    /* DB ERRORS */
    UNIQUE_FIELD_CONSTRAINT(1000, "Unique Field Constraint"),
    REQUESTED_DATA_NOT_FOUND(1001, "Requested Data Not Found"),

    /* REQUESTS ERRORS */
    BAD_REQUEST(2000, "Bad Request data");

    private final int code;
    private final String reasonPhrase;
    private final String errorPrefix = "errorCode";
    private final String titlePostfix = "title";

    ErrorCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return this.code;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String getMessageTitleKey() {
        return this.errorPrefix + "." + this.name() + "." + this.titlePostfix;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + ". Title: " + this.getMessageTitleKey() + ".";
    }
}
