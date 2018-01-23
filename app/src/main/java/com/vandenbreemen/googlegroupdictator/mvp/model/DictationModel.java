package com.vandenbreemen.googlegroupdictator.mvp.model;

import android.content.Context;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.post.api.Post;
import com.vandenbreemen.googlegroupdictator.svc.DictationService;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Flag indicating model is already closed
     */
    private AtomicBoolean closed;

    public DictationModel(List<Post> posts){
        this.utterances = new ArrayList<>();
        this.closed = new AtomicBoolean(false);

        AtomicInteger counter = new AtomicInteger(0);

        posts.forEach(post->{

            utterances.add("Post number "+(counter.incrementAndGet()));
            utterances.add(parseLine(post.getTitle()));
            utterances.add("Body");

            String body = cleanupText(post.getBody());
            String[] lines = body.split("[.!:]");
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
     * Clear line of common junk like URLs or other things we don't care to dictate
     * @param text
     * @return
     */
    private String cleanupText(String text){
        return text.replaceAll("http[s]*[:](//)[^ ]+", "URL");
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
        if(this.alreadyPlayingService != null){
            try{
                this.alreadyPlayingService.close();
            }
            catch(Exception ex){
                Log.e(DictationModel.class.getSimpleName(), "Failed to close", ex);
            }
        }
        this.closed.set(true);
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

    /**
     * Returns true if the model has already been closed
     * @return
     */
    public boolean isClosed(){
        return this.closed.get();
    }
}
