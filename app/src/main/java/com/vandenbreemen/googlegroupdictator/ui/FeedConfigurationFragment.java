package com.vandenbreemen.googlegroupdictator.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vandenbreemen.googlegroupdictator.R;
import com.vandenbreemen.googlegroupdictator.mvp.controller.FeedConfigurationController;
import com.vandenbreemen.googlegroupdictator.mvp.view.FeedConfigurationView;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;
import com.vandenbreemen.googlegroupdictator.util.msg.RuntimeError;

/**
 * <br/>Created by kevin on 06/01/18.
 */
public class FeedConfigurationFragment extends Fragment implements FeedConfigurationView{

    /**
     * Activity using this fragment must provide this
     */
    public static interface FeedConfigCompletionListener {
        /**
         * What to do when feed config is completed
         * @param   groupName   New group name
         */
        void onFeedConfigCompleted(String groupName);
    }

    private FeedConfigurationController controller;

    /**
     * The UI itself
     */
    private ViewGroup ui;

    /**
     * Callback
     */
    private FeedConfigCompletionListener listener;

    /**
     * Configure the controller
     * @param controller
     * @return
     */
    public FeedConfigurationFragment setController(FeedConfigurationController controller) {
        this.controller = controller;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ui = (ViewGroup) inflater.inflate(R.layout.layout_feed_configuration, container, false);

        new OkCancelActions(()->{
            String feedName = ((TextView)ui.findViewById(R.id.groupName)).getText().toString();
            controller.specifyFeedName(feedName);
        }, ()->
            controller.cancel()
        ).assign(ui);

        controller.setView(this);
        controller.start();

        return ui;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FeedConfigCompletionListener){
            this.listener = (FeedConfigCompletionListener) context;
            return;
        }

        throw new RuntimeError("Activity using this fragment must implement "+FeedConfigCompletionListener.class);

    }

    @Override
    public void showError(ApplicationError error) {
        getActivity().runOnUiThread(()-> Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showCurrentGroupName(String currentGroupName) {
        ((EditText)ui.findViewById(R.id.groupName)).setText(currentGroupName);
    }

    @Override
    public void onFeedConfiguredSuccessfully(String groupName) {
        listener.onFeedConfigCompleted(groupName);
    }
}
