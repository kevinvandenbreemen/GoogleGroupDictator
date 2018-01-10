package com.vandenbreemen.googlegroupdictator.svc;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handle speaking the text in the app
 * <br/>Created by kevin on 07/01/18.
 */
public class DictationService implements Closeable, TextToSpeech.OnInitListener {

    /**
     * Callback that anything dictating text must implement
     */
    public static interface DictationCallback{

        /**
         * Signal that next utterance has begun
         * @param position
         */
        void onStartUtterance(int position);

        /**
         * If something goes wrong
         * @param error
         */
        void onError(ApplicationError error);

        /**
         * What should happen when speaking is finished
         */
        void onDoneSpeaking();

    }

    /**
     * What's being dictated
     */
    private List<String> utterances;

    /**
     * Callback we'll be sending to
     */
    private DictationCallback dictationCallback;

    /**
     * App context we're doing this in
     */
    private Context context;

    /**
     * Text to speech engine
     */
    private TextToSpeech textToSpeech;

    /**
     * Current position during dictation
     */
    private AtomicInteger currentPosition;

    /**
     * Indicates TTS is initialized
     */
    private AtomicBoolean initialized;

    /**
     * Define a new dictation service
     * @param context
     */
    public DictationService(Context context, DictationCallback callback) {
        this.context = context;
        this.dictationCallback = callback;
        this.initialized = new AtomicBoolean(false);

        this.textToSpeech = new TextToSpeech(context, this);
        this.initialized.set(true);
    }

    @Override
    public void onInit(int status) {
        System.out.println("INITIALIZED!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        new Throwable().printStackTrace();
        if(status == TextToSpeech.ERROR){
            Log.e(DictationService.class.getSimpleName(), "Error occurred initializing");
            dictationCallback.onError(new ApplicationError("Unexpected error occurred generating speech"));
        }
        else{
            textToSpeech.setLanguage(Locale.US);

            Log.i(DictationService.class.getSimpleName(), "TTS initialized");
        }
    }

    /**
     * Set the utterances to be spoken
     * @param utterances
     * @return
     */
    public DictationService setUtterances(List<String> utterances) {
        this.utterances = utterances;
        return this;
    }

    public void setCurrentPosition(int position){
        this.currentPosition.set(position);
    }

    /**
     * Begin speaking the utterances
     */
    public void speak(){

        AsyncTask<List<String>, Integer, Boolean> speechTask = new AsyncTask<List<String>, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(List<String>[] lists) {

                List<String> toBeSpoken = lists[0];
                currentPosition = new AtomicInteger(0);   //  Position of current utterance

                try {
                    while (currentPosition.get() < toBeSpoken.size()) {

                        int position = currentPosition.get();
                        String toSpeak = toBeSpoken.get(position);

                        while (!initialized.get() || textToSpeech.isSpeaking()) {
                            Thread.sleep(100);
                            //Log.d(DictationService.class.getSimpleName(), "Waiting for TTS availability or next utterance");
                        }

                        publishProgress(position);
                        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "GrpDict_"+position);

                    }
                }catch(InterruptedException interrupted){
                    Log.w(DictationService.class.getSimpleName(), "Interrupted while dictating", interrupted);
                    return false;
                }

                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int progressVal = values[0];
                dictationCallback.onStartUtterance(progressVal);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(!Boolean.TRUE.equals(aBoolean)){
                    dictationCallback.onError(new ApplicationError("An unknown error occurred while dictating"));
                    return;
                }
                dictationCallback.onDoneSpeaking();
            }
        };

        speechTask.execute(this.utterances);

    }

    @Override
    public void close() throws IOException {
        this.textToSpeech.stop();
        this.textToSpeech.shutdown();
    }
}
