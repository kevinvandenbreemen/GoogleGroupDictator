package com.vandenbreemen.googlegroupdictator;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vandenbreemen.googlegroupdictator.mvp.controller.DictationController;
import com.vandenbreemen.googlegroupdictator.mvp.controller.FeedConfigurationController;
import com.vandenbreemen.googlegroupdictator.mvp.controller.FeedController;
import com.vandenbreemen.googlegroupdictator.mvp.model.DictationModel;
import com.vandenbreemen.googlegroupdictator.mvp.model.FeedConfigurationModel;
import com.vandenbreemen.googlegroupdictator.mvp.model.FeedModel;
import com.vandenbreemen.googlegroupdictator.mvp.view.ErrorMessageDisplay;
import com.vandenbreemen.googlegroupdictator.mvp.view.FeedView;
import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.Post;
import com.vandenbreemen.googlegroupdictator.ui.DictationFragment;
import com.vandenbreemen.googlegroupdictator.ui.FeedConfigurationFragment;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Entry point of the app
 */
public class MainActivity extends AppCompatActivity implements ErrorMessageDisplay, FeedConfigurationFragment.FeedConfigCompletionListener,
        FeedView, DictationFragment.OnDictationClosedListener {

    /**
     * Calculated screen width in pixels
     */
    private float screenTranslationDistanceX = 10000;

    /**
     * The feed itself
     */
    private FeedController feedController;

    private FeedConfigurationModel configuration;

    /**
     * Currently displayed fragment
     */
    private Fragment currentFragment;

    /**
     * Hide the current UI inset
     */
    private void hideInset(){
        ((ViewGroup)findViewById(R.id.dialogInset))
                .animate().translationXBy(-screenTranslationDistanceX).setDuration(400);

        findViewById(R.id.settings).setEnabled(true);
        findViewById(R.id.dictate).setEnabled(true);

        getFragmentManager().beginTransaction().remove(currentFragment).commit();
    }

    /**
     * Display the given fragment in the UI inset
     * @param fragment
     */
    private void showUIInset(Fragment fragment){
        ((ViewGroup)findViewById(R.id.dialogInset)).animate().translationXBy(screenTranslationDistanceX).setDuration(400);
        getFragmentManager().beginTransaction().add(R.id.dialogInset, fragment).commit();

        findViewById(R.id.settings).setEnabled(false);
        findViewById(R.id.dictate).setEnabled(false);
        this.currentFragment = fragment;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideLoadingAnimation();

        this.configuration = new FeedConfigurationModel(this);

        //  Hide this by default
        ((ViewGroup)findViewById(R.id.dialogInset)).setTranslationX(-screenTranslationDistanceX);

        //  Make sure we can actually do something!
        if(StringUtils.isBlank(configuration.getGroupName())){
            showUIInset(new FeedConfigurationFragment().setController(new FeedConfigurationController(configuration)));
        }
        else{
            refreshFeed();
        }
    }

    /**
     * Refresh the posts feed
     */
    private void refreshFeed() {
        this.feedController = new FeedController(
                new FeedModel(configuration),
                configuration,
                this
        );
        this.feedController = feedController;
        this.feedController.start();
    }

    @Override
    public void showError(ApplicationError error) {
        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFeedConfigCompleted(String groupName) {
        hideInset();
        refreshFeed();
    }

    @Override
    public void showItems(List<Item> items) {
        ListView listView = findViewById(R.id.itemList);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.layout_feed_item, items){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewGroup view = (ViewGroup) (convertView != null ? convertView
                                        : getLayoutInflater().inflate(R.layout.layout_feed_item, listView, false));

                TextView tv;

                tv = view.findViewById(R.id.author);
                tv.setText(getItem(position).getAuthor());

                tv = view.findViewById(R.id.postTitle);
                tv.setText(getItem(position).getTitle());

                tv = view.findViewById(R.id.date);
                Date date = new Date();
                date.setTime(getItem(position).getPubDate());
                tv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));

                CheckBox checkBox = view.findViewById(R.id.itemSelector);
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(feedController.isSelected(getItem(position)));
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> feedController.select(getItem(position)));

                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    @Override
    public void showLoadingAnimation() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingAnimation() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    public void onDictate(View view) {
        feedController.dictate();
    }

    public void onConfig(View view) {
        showUIInset(new FeedConfigurationFragment().setController(new FeedConfigurationController(configuration)));
    }

    @Override
    public void onDictatePosts(List<Post> postsToDictate) {
        DictationModel model = new DictationModel(postsToDictate);
        model.setContext(this);
        DictationFragment fragment = new DictationFragment();
        fragment.setController(new DictationController(model));
        showUIInset(fragment);
    }

    @Override
    public void onCloseDictation() {
        hideInset();
    }
}
