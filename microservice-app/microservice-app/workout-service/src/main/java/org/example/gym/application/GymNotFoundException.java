package org.example.gym.application;

public class GymNotFoundException extends RuntimeException {
    public GymNotFoundException() {
    }

    public GymNotFoundException(String message) {
        super(message);
    }

    public GymNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
