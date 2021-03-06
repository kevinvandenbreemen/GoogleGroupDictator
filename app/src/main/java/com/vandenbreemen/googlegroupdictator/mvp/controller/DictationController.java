package com.vandenbreemen.googlegroupdictator.mvp.controller;

import com.vandenbreemen.googlegroupdictator.mvp.model.DictationModel;
import com.vandenbreemen.googlegroupdictator.mvp.view.DictationView;
import com.vandenbreemen.googlegroupdictator.svc.DictationService;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

/**
 * Dictation
 * <br/>Created by kevin on 07/01/18.
 */
public class DictationController implements DictationService.DictationCallback{

    private DictationModel model;

    private DictationView view;

    public DictationController(DictationModel model) {
        this.model = model;
    }

    /**
     * Configure the underlying view
     * @param view
     * @return
     */
    public DictationController setView(DictationView view) {
        this.view = view;
        return this;
    }


    public void start() {
        view.setMax(model.getMaxUtterances());
    }

    public void onClose() {
        if(!model.isClosed()) {
            model.close();
            view.onDone();
        }
    }

    public void onPause() {
        model.pause();
    }

    public void onPlay() {
        try{
            this.model.speak(this);
        }
        catch(ApplicationError ex){
            view.showError(ex);
        }
    }

    /**
     * Update the current position of dictation
     * @param position
     */
    public void setCurrentPosition(int position){
        this.model.setPosition(position);
    }

    @Override
    public void onStartUtterance(int position) {
        view.updatePosition(position);
    }

    @Override
    public void onError(ApplicationError error) {
        view.showError(error);
    }

    @Override
    public void onDoneSpeaking() {
        onClose();
    }
}
