package com.vandenbreemen.googlegroupdictator.mvp.view;

import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

/**
 * Interface to implement in order to show error messages
 * <br/>Created by kevin on 06/01/18.
 */
public interface ErrorMessageDisplay {

    /**
     * Display the given error message
     * @param error
     */
    void showError(ApplicationError error);

}
