package com.vandenbreemen.googlegroupdictator.mvp.view;

/**
 * <br/>Created by kevin on 07/01/18.
 */
public interface DictationView extends ErrorMessageDisplay{

    /**
     * Set the maximum number of "sentences" that will be spoken as part of dictation
     * @param maxUtterances
     */
    void setMax(int maxUtterances);

    /**
     * Update the current position in the text being read aloud
     * @param position
     */
    void updatePosition(int position);

    /**
     * Action to take place when speech is completed
     */
    void onDone();
}
