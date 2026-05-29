package com.eduardozava.javaia.infra.deepseek;

public class DeepSeekClientException extends RuntimeException {
    public DeepSeekClientException(String message) {
        super(message);
    }

    public DeepSeekClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
