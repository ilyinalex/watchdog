package edu.ilin.watchdog.exception;

import org.springframework.http.HttpStatus;

public class InternalException extends RuntimeException {
    private HttpStatus httpStatus;

    public InternalException() {
    }

    public InternalException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    protected InternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
