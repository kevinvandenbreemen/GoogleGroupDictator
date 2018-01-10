package com.vandenbreemen.googlegroupdictator.post.api;

/**
 * An item in the RSS feed
 * <br/>Created by kevin on 06/01/18.
 */
public class Item {

    private final String title;
    private final String link;
    private final String description;
    private final long pubDate;
    private final String author;

    public Item(String title, String link, String description, long pubDate, String author) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public long getPubDate() {
        return pubDate;
    }

    public String getAuthor() {
        return author;
    }
}
