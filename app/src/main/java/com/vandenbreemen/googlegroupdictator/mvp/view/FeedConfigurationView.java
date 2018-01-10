package com.vandenbreemen.googlegroupdictator.mvp.view;

import com.vandenbreemen.googlegroupdictator.mvp.controller.FeedConfigurationController;

/**
 * Created by kevin on 06/01/18.
 */

public interface FeedConfigurationView extends ErrorMessageDisplay {

    /**
     * Display the currently configured group name
     * @param currentGroupName
     */
    void showCurrentGroupName(String currentGroupName);

    /**
     * Feed has been successfully configured
     */
    void onFeedConfiguredSuccessfully(String groupName);
}
