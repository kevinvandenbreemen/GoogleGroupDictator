package com.vandenbreemen.googlegroupdictator.util;

/**
 * Exception-aware supplier
 * <br/>Created by kevin on 06/01/18.
 */
public interface Supplier<T> {

    /**
     * Compute, possibly throwing an exception
     * @return
     * @throws Exception
     */
    public T get() throws Exception;

}
