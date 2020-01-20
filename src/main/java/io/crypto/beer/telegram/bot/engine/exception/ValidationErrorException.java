package io.crypto.beer.telegram.bot.engine.exception;

public class ValidationErrorException extends RuntimeException {
    private static final long serialVersionUID = 401658313237901150L;

    public ValidationErrorException() {
        super();
    }

    public ValidationErrorException(String errorMessage) {
        super(errorMessage);
    }
}
