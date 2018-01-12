package com.vandenbreemen.googlegroupdictator.mvp.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by kevin on 11/01/18.
 */
@RunWith(RobolectricTestRunner.class)
public class FeedConfigurationModelTest {

    /**
     * Name of group.  This test goes out on the internet and polls the group's RSS feed so this should be a
     * real Google group
     */
    private static final String GROUP_NAME = "yt-ph";

    @Test
    public void testConfigureGroup() throws Exception{
        FeedConfigurationModel model = new FeedConfigurationModel(RuntimeEnvironment.application);
        model.setGroupName(GROUP_NAME);
        assertThat("Group name configured", model.getGroupName(), equalTo(GROUP_NAME));
    }

    @Test
    public void testDefaultGroupNameBlank(){
        FeedConfigurationModel model = new FeedConfigurationModel(RuntimeEnvironment.application);
        assertThat("Default group is blank", model.getGroupName(), is(blankString()));
    }

}
