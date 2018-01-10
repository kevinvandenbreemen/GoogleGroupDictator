package com.vandenbreemen.googlegroupdictator.post.impl;

import java.io.Closeable;
import java.io.InputStream;

/**
 * Provides stream to XML for RSS feed reader
 * <br/>Created by kevin on 06/01/18.
 */
public interface XMLStreamProvider {

    /**
     * A handle to the input stream of the underlying source.  Implementations MUST close out any and all
     * resources or connections held in order to allow this handle to function
     */
    interface StreamHandle extends Closeable{

        /**
         * Get input stream
         * @return
         */
        public InputStream getInputStream();
    }

    /**
     * Configure the number of messages to be retrieved when this provider is used
     * @param numMessagesToRetrieve
     */
    XMLStreamProvider setNumMessagesToRetrieve(int numMessagesToRetrieve);

    /**
     * Get a closeable handle to a stream.  Closing the handle will clean up all lingering resources/connections etc.
     * @return
     */
    StreamHandle getStreamHandle();

}
