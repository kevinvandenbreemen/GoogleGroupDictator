package com.vandenbreemen.googlegroupdictator.post.api;

/**
 * A post to the group
 * <br/>Created by kevin on 06/01/18.
 */
public class Post {

    private String body;

    public Post(String body) {
        this.body = body;
    }

    /**
     * Get the body of the given post
     * @return
     */
    public String getBody() {
        return body;
    }
}
