package com.shop.exception;

public class CustomException extends Exception {
    public enum ErrorType {
        ALL_IMAGES_DELETED,
        INVALID_IMAGE_FORMAT
    }

    private final ErrorType errorType;

    public CustomException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
