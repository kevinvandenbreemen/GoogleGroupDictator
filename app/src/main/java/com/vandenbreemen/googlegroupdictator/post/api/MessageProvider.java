package com.vandenbreemen.googlegroupdictator.post.api;

import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import java.util.List;

/**
 * Provides a list of messages from the group
 * <br/>Created by kevin on 06/01/18.
 */
public interface MessageProvider {

    /**
     * Get list of messages currently available
     * @return
     */
    public List<Item> getMessages();

    /**
     * Fet the post corresponding to the given item
     * @param item
     * @return
     * @throws  ApplicationError    If something goes wrong fetching the message content
     */
    public Post getPost(Item item) throws ApplicationError;

}
