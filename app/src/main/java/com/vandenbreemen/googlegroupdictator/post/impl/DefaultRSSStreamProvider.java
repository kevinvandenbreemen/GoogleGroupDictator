package com.vandenbreemen.googlegroupdictator.post.impl;

import android.util.Log;

import com.vandenbreemen.googlegroupdictator.util.msg.RuntimeError;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connect to Google Groups and retrieve XML stream
 * <br/>Created by kevin on 06/01/18.
 */
public class DefaultRSSStreamProvider implements XMLStreamProvider {

    private String feedName;

    /**
     * Number of messages to be retrieved
     */
    private int numMessagesToRetrieve;

    /**
     * Create RSS stream provider with the given feed name
     * @param feedName
     */
    public DefaultRSSStreamProvider(String feedName) {
        this.feedName = feedName;
        this.numMessagesToRetrieve = 15;
    }

    @Override
    public XMLStreamProvider setNumMessagesToRetrieve(int numMessagesToRetrieve) {
        this.numMessagesToRetrieve = numMessagesToRetrieve;
        return this;
    }

    @Override
    public StreamHandle getStreamHandle() {

        StringBuilder urlStr = new StringBuilder("https://groups.google.com/forum/feed/");
        urlStr.append(feedName).append("/msgs/rss.xml?num="+numMessagesToRetrieve);
        HttpURLConnection connection;

        try{
            URL url = new URL(urlStr.toString());
            connection = (HttpURLConnection) url.openConnection();
        }
        catch(IOException ex){
            Log.e(DefaultRSSStreamProvider.class.getSimpleName(), "Failed to obtain connection or the stream", ex);
            throw new RuntimeError("Failed to connect to the stream", ex);
        }

        return new StreamHandle() {

            @Override
            public InputStream getInputStream() {
                try{
                    URL url = new URL(urlStr.toString());
                    return new BufferedInputStream(url.openConnection().getInputStream());
                }
                catch(Exception ex){
                    Log.e(DefaultRSSStreamProvider.class.getSimpleName(), "Failed to obtain connection or the stream", ex);
                    throw new RuntimeError("Failed to access stream", ex);
                }
            }

            @Override
            public void close() throws IOException {
                connection.disconnect();
            }
        };


    }
}
