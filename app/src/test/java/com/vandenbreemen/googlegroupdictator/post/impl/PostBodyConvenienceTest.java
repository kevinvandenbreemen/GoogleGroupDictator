package com.vandenbreemen.googlegroupdictator.post.impl;

import org.junit.Test;

/**
 * Created by kevin on 06/01/18.
 */
//  Convenience unit test to make it easier for me to debug post display issues
public class PostBodyConvenienceTest {

    @Test
    public void testDisplayOfText() throws Exception{
        RSSMessageProvider provider = new RSSMessageProvider();
        String url = "https://groups.google.com/d/msg/rec.arts.startrek.current/irsbNwNKnL0/m9k-Dq75BAAJ";

        String content = provider.getPostBody(url);
        System.out.println("CONTENT\n========================\n");
        System.out.println(content);
    }

}
