package io.github.jfelixy.concurrentlabs.exceptions;

public class TeacherAlreadyEnrolledException extends RuntimeException {
    public TeacherAlreadyEnrolledException(String message) {
        super(message);
    }
}
