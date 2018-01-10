package com.vandenbreemen.googlegroupdictator.mvp.model;

import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.MessageProvider;
import com.vandenbreemen.googlegroupdictator.post.api.Post;
import com.vandenbreemen.googlegroupdictator.post.impl.DefaultRSSStreamProvider;
import com.vandenbreemen.googlegroupdictator.post.impl.RSSMessageProvider;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Feed of posts
 * <br/>Created by kevin on 06/01/18.
 */
public class FeedModel {

    /**
     * Message provider for getting all the posts
     */
    private MessageProvider provider;

    /**
     * Items the user has selected
     */
    private List<Item> selectedItems;

    public FeedModel(FeedConfigurationModel configurationModel){
        this.provider = new RSSMessageProvider().setStream(new DefaultRSSStreamProvider(configurationModel.getGroupName()));
        this.selectedItems = new ArrayList<>();
    }

    /**
     * Get all the posts available
     * @return
     */
    public List<Item> getPosts(){
        return this.provider.getMessages();
    }

    /**
     * Get the given post
     * @param item
     * @return
     */
    public Post getPost(Item item) throws ApplicationError {
        return this.provider.getPost(item);
    }

    /**
     * Close this model
     */
    public void close(){

    }

    public void select(Item item){
        this.selectedItems.add(item);
    }

    public void deselect(Item item){
        this.selectedItems.remove(item);
    }

    public boolean isSelected(Item item){
        return this.selectedItems.contains(item);
    }

    /**
     * Get all the currently-selected items
     * @return
     */
    public List<Item> getSelectedItems(){
        return Collections.unmodifiableList(this.selectedItems);
    }

    /**
     * Get all selected items as posts
     * @return
     */
    public List<Post> getSelectedItemsAsPosts() throws ApplicationError{
        List<Post> posts = new ArrayList<>();
        for(Item item : getSelectedItems()){
            posts.add(provider.getPost(item));
        }

        return posts;
    }

}
