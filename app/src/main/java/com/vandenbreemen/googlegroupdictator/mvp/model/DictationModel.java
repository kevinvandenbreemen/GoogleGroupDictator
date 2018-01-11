package com.vandenbreemen.googlegroupdictator.mvp.model;

import android.content.Context;

import com.vandenbreemen.googlegroupdictator.post.api.Post;
import com.vandenbreemen.googlegroupdictator.svc.DictationService;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <br/>Created by kevin on 07/01/18.
 */
public class DictationModel {

    /**
     * Utterances that are to be made
     */
    private List<String> utterances;

    /**
     * Context for issuing requests to dictation
     */
    private Context context;

    /**
     * What's already playing!
     */
    private DictationService alreadyPlayingService;

    public DictationModel(List<Post> posts){
        this.utterances = new ArrayList<>();

        posts.forEach(post->{
            String[] lines = post.getBody().split("[.!:]");
            Arrays.stream(lines).forEach(line->{
                if(!StringUtils.isBlank(line)){
                    String parsed = parseLine(line);
                    if(!StringUtils.isBlank(parsed)){
                        utterances.add(parsed);
                    }
                }
            });
        });

        //  Prevent modification of the utterances now that they're set
        this.utterances = Collections.unmodifiableList(utterances);

    }

    /**
     * Set the context.  This is critical for the model to be used
     * @param context
     * @return
     */
    public DictationModel setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * Parse the given line
     * @param line
     * @return
     */
    private String parseLine(String line){
        String escaped = StringEscapeUtils.unescapeHtml4(line);
        escaped = escaped.replaceAll("[><*:]", "");

        //  get rid of ellipses
        escaped = escaped.replaceAll("[.]{2,}", ".");

        //  Finally remove line-greaks
        escaped = escaped.replaceAll("[\n]", "");

        return escaped.trim();
    }

    List<String> getUtterances(){
        return this.utterances;
    }

    /**
     * Pause playback
     */
    public void pause(){

    }

    /**
     * Shut down speech and close up the model
     */
    public void close(){

    }

    public int getMaxUtterances() {
        return this.utterances.size();
    }

    /**
     * Update the position of dictation to the given position
     * @param position
     */
    public void setPosition(int position){
        this.alreadyPlayingService.setCurrentPosition(position);
    }

    /**
     * Speak the post(s)
     * @param callback  Callback for various events
     */
    public void speak(DictationService.DictationCallback callback) throws ApplicationError{

        if(this.alreadyPlayingService != null){
            throw new ApplicationError("Already dictating");
        }

        DictationService service = new DictationService(context, callback);
        service.setUtterances(this.utterances);
        service.speak();
        this.alreadyPlayingService = service;
    }
}
