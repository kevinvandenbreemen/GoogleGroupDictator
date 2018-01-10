package com.vandenbreemen.googlegroupdictator.mvp.controller;

import com.vandenbreemen.googlegroupdictator.mvp.model.FeedConfigurationModel;
import com.vandenbreemen.googlegroupdictator.mvp.model.FeedModel;
import com.vandenbreemen.googlegroupdictator.mvp.view.FeedView;
import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.util.DoAndThen;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * For the feed itself
 * <br/>Created by kevin on 06/01/18.
 */
public class FeedController implements Controller{

    private FeedModel model;

    /**
     * Configuration of the system
     */
    private FeedConfigurationModel configurationModel;
    private FeedView view;

    public FeedController(FeedModel model, FeedConfigurationModel configurationModel, FeedView view) {
        this.model = model;
        this.view = view;
        this.configurationModel = configurationModel;
    }

    @Override
    public void start() {
        view.showLoadingAnimation();
        DoAndThen.run(()->model.getPosts(),
                items->{
                    view.showItems(items);
                    view.hideLoadingAnimation();
                },
                (error)->{
                    view.showError(error);
                    view.hideLoadingAnimation();
                }
                );
    }

    @Override
    public void close() {
        this.model.close();
    }

    /**
     * Check to see if the given item has been selected in the past
     * @param item
     * @return
     */
    public boolean isSelected(Item item){
        return this.model.isSelected(item);
    }

    /**
     * Select / deselect the given item
     * @param item
     */
    public void select(Item item){
        if(!this.model.isSelected(item)) {
            this.model.select(item);
        }
        else{
            this.model.deselect(item);
        }
    }

    /**
     * Start dictating the selected posts
     */
    public void dictate(){
        List<Item> items = model.getSelectedItems();
        if(CollectionUtils.isEmpty(items)){
            view.showError(new ApplicationError("No items selected"));
            return;
        }

        view.showLoadingAnimation();
        DoAndThen.run(()->model.getSelectedItemsAsPosts(),
                    posts -> {
                        view.onDictatePosts(posts);
                        view.hideLoadingAnimation();
                    },
                    error -> {
                        view.showError(error);
                        view.hideLoadingAnimation();
                    }
                );
    }
}
