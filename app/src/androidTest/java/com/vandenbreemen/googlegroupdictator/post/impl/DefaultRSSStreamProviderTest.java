package com.vandenbreemen.googlegroupdictator.post.impl;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.Post;

import junit.framework.TestCase;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.fail;

/**
 * Created by kevin on 06/01/18.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class DefaultRSSStreamProviderTest {

    /**
     * Name of group.  This test goes out on the internet and polls the group's RSS feed so this should be a
     * real Google group
     */
    private static final String GROUP_NAME = "yt-ph";

    @Test
    public void sanityTestGetFeed(){
        DefaultRSSStreamProvider provider = new DefaultRSSStreamProvider(GROUP_NAME);

        try(XMLStreamProvider.StreamHandle handle = provider.getStreamHandle()){
            assertNotNull("Stream expected", handle.getInputStream());
        }
        catch(Exception ex){
            ex.printStackTrace();
            fail("Unexpected error");
        }
    }

    @Test
    public void testGetItemsReturnsList(){
        RSSMessageProvider provider = new RSSMessageProvider();
        provider.setStream(new DefaultRSSStreamProvider(GROUP_NAME));

        List<Item> items = provider.getMessages();

        assertFalse("Items expected", CollectionUtils.isEmpty(items));
    }

    @Test
    public void checkAllItemsForContent(){
        RSSMessageProvider provider = new RSSMessageProvider();
        provider.setStream(new DefaultRSSStreamProvider(GROUP_NAME));

        List<Item> items = provider.getMessages();

        items.forEach(item->{
            assertNotNull(item.getAuthor());
            assertNotNull(item.getDescription());
            assertNotNull(item.getLink());
            assertNotNull(item.getTitle());
            assertNotSame(0L, item.getPubDate());
        });
    }

    /**
     * System must be able to then fetch the post body for an individual message
     */
    @Test
    public void sanityTestFetchPost() throws Exception{
        RSSMessageProvider provider = new RSSMessageProvider();
        provider.setStream(new DefaultRSSStreamProvider(GROUP_NAME));

        List<Item> items = provider.getMessages();

        Post post = provider.getPost(items.get(0));

        assertNotNull("Post expected", post);

    }

    //  Allow configuration of number of items to fetch
    @Test
    public void testSetNumberOfItemsToRetrieve() throws Exception{
        RSSMessageProvider provider = new RSSMessageProvider();
        provider.setStream(new DefaultRSSStreamProvider(GROUP_NAME).setNumMessagesToRetrieve(20));

        List<Item> items = provider.getMessages();
        assertEquals("20 items expected", 20, items.size());
    }

    @Test
    public void testFetchKnownPostData() throws Exception{
        String url = "https://groups.google.com/d/msg/yt-ph/tHFIt9N3LGI/vZqrSzcsBwAJ";
        RSSMessageProvider provider = new RSSMessageProvider();

        String content = provider.getPostBody(url);
        assertTrue("Content should start with expected string",
                content.startsWith("Request 1 by MercutioLives"));
    }

    @Test
    public void testFetchKnownPostDataCompleteText() throws Exception{
        String url = "https://groups.google.com/d/msg/yt-ph/tHFIt9N3LGI/vZqrSzcsBwAJ";
        RSSMessageProvider provider = new RSSMessageProvider();

        String content = provider.getPostBody(url);
        assertTrue("Content should end with expected string",
                content.endsWith("Thank you and good luck!"));
    }

    @Test
    public void testNonExistentGroupName() throws Exception{
        DefaultRSSStreamProvider provider = new DefaultRSSStreamProvider("test-test-test-nonexistent-test");
        try{
            provider.getStreamHandle().getInputStream();
            fail("This should not have worked");
        }
        catch(Exception ex){

        }
    }

}
