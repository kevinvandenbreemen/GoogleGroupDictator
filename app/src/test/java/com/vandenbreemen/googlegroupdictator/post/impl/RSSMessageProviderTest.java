package com.vandenbreemen.googlegroupdictator.post.impl;

import com.vandenbreemen.googlegroupdictator.post.api.Item;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by kevin on 06/01/18.
 */
public class RSSMessageProviderTest {

    /**
     * Flags that the handle was closed at end of access
     */
    private boolean closed;

    private RSSMessageProvider sut;

    @Before
    public void setup(){
        sut = new RSSMessageProvider();
        closed = false;

        try{
            sut.setStream(new XMLStreamProvider() {
                @Override
                public XMLStreamProvider setNumMessagesToRetrieve(int numMessagesToRetrieve) {
                    return this;
                }

                @Override
                public StreamHandle getStreamHandle() {
                    return new StreamHandle() {
                        @Override
                        public InputStream getInputStream() {
                            try{
                                return new FileInputStream("testResource/rsstest.xml");
                            }
                            catch(Exception ex){
                                throw new RuntimeException("Failed to get stream");
                            }
                        }

                        @Override
                        public void close() throws IOException {
                            closed = true;
                        }
                    };
                }
            });
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Failed to read in RSS Feed Data");
        }
    }

    @Test
    public void testClosesStream(){
        List<Item> items = sut.getMessages();
        assertTrue("System should have closed the stream", closed);
    }

    @Test
    public void sanityTestParsePostsContainsPosts(){
        List<Item> items = sut.getMessages();
        assertFalse("Items expected", CollectionUtils.isEmpty(items));
    }

    @Test
    public void testItemsContainPublicationDate(){
        List<Item> items = sut.getMessages();
        Item item = items.get(0);

        long pubDate = item.getPubDate();
        Date pDate = new Date();
        pDate.setTime(pubDate);
        String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(pDate);

        assertEquals("Pub date should be a set", "Sat, 06 Jan 2018 07:37:37 EST", formatted);
    }

    @Test
    public void testGetsTitleOfPost(){
        List<Item> items = sut.getMessages();
        Item item = items.get(0);
        assertEquals("Title expected", "Re: What is Worf's first name?", item.getTitle());
    }

    @Test
    public void testGetsAuthorOfPost(){
        List<Item> items = sut.getMessages();
        Item item = items.get(0);
        assertEquals("Author expected", "John Savard", item.getAuthor());
    }

    @Test
    public void testGetsDescriptionOfPost(){
        List<Item> items = sut.getMessages();
        Item item = items.get(0);

        assertEquals(
                "Description missing",
                "On Monday, January 31, 1994 at 11:37:37 AM UTC-7, Craig Allen Campbell\n" +
                        "                wrote: > If > you want to get cute, it's Worf Rojhenko-Mogson. :) What,\n" +
                        "                not Worf Mogovich Rodzhenko? John Savard",
                item.getDescription()
        );
    }

    @Test
    public void testGetsLinkForPost(){
        List<Item> items = sut.getMessages();
        Item item = items.get(0);
        assertEquals("Link expected", "https://groups.google.com/d/msg/rec.arts.startrek.current/cl1xtxNzc8s/Bn3XS0AZDAAJ",
                item.getLink());
    }



}
