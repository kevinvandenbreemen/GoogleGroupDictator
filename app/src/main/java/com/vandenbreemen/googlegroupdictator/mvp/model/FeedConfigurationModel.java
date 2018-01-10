package com.vandenbreemen.googlegroupdictator.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.post.impl.DefaultRSSStreamProvider;
import com.vandenbreemen.googlegroupdictator.post.impl.XMLStreamProvider;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * Configuration for the feed we'll be using
 * <br/>Created by kevin on 06/01/18.
 */
public class FeedConfigurationModel {

    /**
     * Group name
     */
    public static final String KEY_GROUP_NAME = "__group_name";
    public static final String SHARED_PREFERENCES_NAME = "com.vandenbreemen.googlegroupdictator";

    /**
     * App context
     */
    private Context context;

    /**
     *
     * @param context
     */
    public FeedConfigurationModel(Context context) {
        this.context = context;
    }

    /**
     * Get the name of the group
     * @return
     */
    public String getGroupName() {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_GROUP_NAME, "");
    }

    /**
     * Set the group name
     * @param groupName
     * @return
     */
    public void setGroupName(String groupName) throws ApplicationError{
        if(StringUtils.isBlank(groupName)){
            throw new ApplicationError("Group name cannot be empty");
        }

        //  Now validate that the group name makes sense
        DefaultRSSStreamProvider provider = new DefaultRSSStreamProvider(groupName);
        try(XMLStreamProvider.StreamHandle handle = provider.getStreamHandle(); InputStream strea = handle.getInputStream()){
        }
        catch(Exception ex){
            Log.e(FeedConfigurationModel.class.getSimpleName(), "Failed to get stream", ex);
            throw new ApplicationError("Unable to connect to this group's feed");
        }

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_GROUP_NAME, groupName).commit();
    }
}
