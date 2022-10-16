package com.iptiq.exceptions;

public class MaxCapacityReachedException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of the class.
     *
     * @param message Message explaining the exception.
     */
    public MaxCapacityReachedException(final String message) {
        super(message);
    }
}
