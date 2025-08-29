package io.github.jfelixy.concurrentlabs.exceptions;

public class SendEmailFailedException extends RuntimeException {
    public SendEmailFailedException(String message) {
        super(message);
    }
}
