package com.vandenbreemen.googlegroupdictator.mvp.controller;

/**
 * Basic behaviour that any controller should provide
 * <br/>Created by kevin on 06/01/18.
 */
public interface Controller {

    /**
     * Start the actual workflow
     */
    void start();

    /**
     * Clean up any data and end the workflow
     */
    void close();

}
