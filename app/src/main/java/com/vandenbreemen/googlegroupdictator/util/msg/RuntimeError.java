package com.vandenbreemen.googlegroupdictator.util.msg;

/**
 * Specialized runtime exception
 * <br/>Created by kevin on 06/01/18.
 */
public class RuntimeError extends RuntimeException {

    public RuntimeError(String message) {
        super(message);
    }

    public RuntimeError(String message, Throwable cause) {
        super(message, cause);
    }
}
