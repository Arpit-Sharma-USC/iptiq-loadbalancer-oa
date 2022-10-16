package com.iptiq.exceptions;

public class UnavailableProviderException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of the class.
     *
     * @param message Message explaining the exception.
     */
    public UnavailableProviderException(final String message) {
        super(message);
    }
}
