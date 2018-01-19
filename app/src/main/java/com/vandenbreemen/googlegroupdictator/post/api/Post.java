package com.vandenbreemen.googlegroupdictator.post.api;

/**
 * A post to the group
 * <br/>Created by kevin on 06/01/18.
 */
public class Post {

    /**
     * Original RSS feed item
     */
    private final Item item;

    private final String body;

    public Post(Item item, String body) {
        this.item = item;
        this.body = body;
    }

    /**
     * Get the body of the given post
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     * Get the title of the post
     * @return
     */
    public String getTitle(){
        return this.item.getTitle();
    }
}
