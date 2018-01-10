package com.vandenbreemen.googlegroupdictator.util;

import android.os.AsyncTask;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Convenient handle to running something as an async task
 * <br/>Created by kevin on 06/01/18.
 */
public class DoAndThen {

    private DoAndThen(){}

    /**
     * Execute task
     * @param supplier  Some kind of long-running operation
     * @param consumer  What to do with the result
     * @param onError   Action that should be taken if an exception is thrown
     * @param <T>
     */
    public static <T> void run(Supplier<T> supplier, Consumer<T> consumer, Consumer<ApplicationError> onError){

        new AsyncTask<Void, Void, T>(){

            /**
             * Error that was thrown
             */
            ApplicationError errorThrown;

            @Override
            protected T doInBackground(Void... voids) {
                try{
                    return supplier.get();
                }
                catch(Exception ex){
                    Log.e(DoAndThen.class.getSimpleName(), "Failed to execute task", ex);
                    if(ex instanceof ApplicationError) {
                        errorThrown = (ApplicationError) ex;
                    }
                    else{
                        errorThrown = new ApplicationError("Unknown error occurred");
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(T result) {

                if(result == null && onError != null && errorThrown != null){
                    onError.accept(errorThrown);
                    return;
                }

                consumer.accept(result);
            }
        }.execute();
    }

    /**
     * Execute a task without any potential exception
     * @param supplier
     * @param consumer
     * @param <T>
     */
    public static <T> void run(Supplier<T> supplier, Consumer<T> consumer){
        run(supplier, consumer, null);
    }

}
