package com.vandenbreemen.googlegroupdictator.mvp.view;

import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.Post;

import java.util.List;

/**
 * Created by kevin on 06/01/18.
 */

public interface FeedView extends ErrorMessageDisplay {

    /**
     * Display all the items
     * @param items
     */
    void showItems(List<Item> items);

    /**
     * Turn on loading animation
     */
    void showLoadingAnimation();

    /**
     * Hide the loading animation
     */
    void hideLoadingAnimation();

    /**
     * Tie into actually dictating the list of posts
     * @param postsToDictate
     */
    void onDictatePosts(List<Post> postsToDictate);
}
