package com.vandenbreemen.googlegroupdictator.mvp.controller;

import com.vandenbreemen.googlegroupdictator.mvp.model.FeedConfigurationModel;
import com.vandenbreemen.googlegroupdictator.mvp.view.FeedConfigurationView;
import com.vandenbreemen.googlegroupdictator.util.DoAndThen;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.lang3.StringUtils;

/**
 * Configuration for the feed
 * <br/>Created by kevin on 06/01/18.
 */
public class FeedConfigurationController implements Controller{

    private FeedConfigurationModel model;

    private FeedConfigurationView view;

    /**
     *
     * @param model
     * @param view
     */
    public FeedConfigurationController(FeedConfigurationModel model) {
        this.model = model;
    }

    /**
     * Configure the view
     * @param view
     * @return
     */
    public FeedConfigurationController setView(FeedConfigurationView view) {
        this.view = view;
        return this;
    }

    /**
     * Start the workflow
     */
    @Override
    public void start(){
        view.showCurrentGroupName(model.getGroupName());
    }

    /**
     * Specify the feed name
     * @param name
     */
    public void specifyFeedName(String name){
        DoAndThen.run(() -> {
            model.setGroupName(name);
            return model.getGroupName();
        }, (updatedName) -> view.onFeedConfiguredSuccessfully(updatedName), (ex) -> view.showError(ex));
    }

    /**
     * Cancel action
     */
    public void cancel(){
        if(StringUtils.isBlank(model.getGroupName())){
            view.showError(new ApplicationError("The group name has not yet been set up.  You cannot use the application without setting this up"));
            return;
        }
        view.onFeedConfiguredSuccessfully(model.getGroupName());
    }

    /**
     * Close any stuff that must be closed
     */
    @Override
    public void close(){

    }
}
