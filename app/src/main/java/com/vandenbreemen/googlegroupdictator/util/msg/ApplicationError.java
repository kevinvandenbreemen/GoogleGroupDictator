package com.vandenbreemen.googlegroupdictator.util.msg;

/**
 * An error thrown by the application code
 * <br/>Created by kevin on 06/01/18.
 */
public class ApplicationError extends Exception {

    public ApplicationError(String message) {
        super(message);
    }

    public ApplicationError(String message, Throwable cause) {
        super(message, cause);
    }
}
