package com.vandenbreemen.googlegroupdictator;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vandenbreemen.googlegroupdictator.mvp.model.FeedConfigurationModelTest;
import com.vandenbreemen.googlegroupdictator.ui.FeedConfigurationFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by kevin on 11/01/18.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Before
    public void setup(){
        ShadowLog.stream = System.out;
    }

    @Test
    public void testPromptsForRSSFeed(){
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        Fragment frag = activity.getFragmentManager().findFragmentById(R.id.dialogInset);

        assertNotNull("Fragment expected", frag);
        assertTrue("Feed configuration UI expected since feed is not set up",
                frag instanceof FeedConfigurationFragment);
    }

    @Test
    public void testConfigureFeed(){
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        TextView textView = activity.findViewById(R.id.groupName);
        textView.setText(FeedConfigurationModelTest.GROUP_NAME);

        Button button = activity.findViewById(R.id.ok);
        button.performClick();

        assertNull(activity.getFragmentManager().findFragmentById(R.id.dialogInset));
    }

    @Test
    public void testRetrievePosts(){
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        TextView textView = activity.findViewById(R.id.groupName);
        textView.setText(FeedConfigurationModelTest.GROUP_NAME);

        Button button = activity.findViewById(R.id.ok);
        button.performClick();

        Shadows.shadowOf(RuntimeEnvironment.application).runBackgroundTasks();

        ListView listView = activity.findViewById(R.id.itemList);
        ShadowListView shadowListView = Shadows.shadowOf(listView);
        shadowListView.populateItems();

        assertNotNull("Cannot test this - unable to get list of items", listView.getAdapter());
        assertNotSame("Items now expected", 0, listView.getAdapter().getCount());

    }

}
