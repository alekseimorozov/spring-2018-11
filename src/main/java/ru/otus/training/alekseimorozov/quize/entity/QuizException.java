package ru.otus.training.alekseimorozov.quize.entity;

public class QuizException extends RuntimeException {
    public QuizException() {
    }

    public QuizException(String message) {
        super(message);
    }

    public QuizException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizException(Throwable cause) {
        super(cause);
    }

    public QuizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}