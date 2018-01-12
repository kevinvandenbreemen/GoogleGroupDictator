package com.vandenbreemen.googlegroupdictator;

import android.app.Activity;
import android.app.Fragment;

import com.vandenbreemen.googlegroupdictator.ui.FeedConfigurationFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by kevin on 11/01/18.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void testPromptsForRSSFeed(){
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        Fragment frag = activity.getFragmentManager().findFragmentById(R.id.dialogInset);

        assertNotNull("Fragment expected", frag);
        assertTrue("Feed configuration UI expected since feed is not set up",
                frag instanceof FeedConfigurationFragment);
    }

}
