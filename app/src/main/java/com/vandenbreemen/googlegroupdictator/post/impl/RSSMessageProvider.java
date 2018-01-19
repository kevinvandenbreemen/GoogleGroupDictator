package com.vandenbreemen.googlegroupdictator.post.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.MessageProvider;
import com.vandenbreemen.googlegroupdictator.post.api.Post;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;
import com.vandenbreemen.googlegroupdictator.util.msg.RuntimeError;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * <br/>Created by kevin on 06/01/18.
 */
public class RSSMessageProvider implements MessageProvider {

    /**
     * XML input stream
     */
    private XMLStreamProvider stream;

    /**
     * Specify input stream to use for getting XML
     * @param stream
     * @return
     */
    public RSSMessageProvider setStream(XMLStreamProvider stream) {
        this.stream = stream;
        return this;
    }

    @Override
    public List<Item> getMessages() {
        SAXReader reader = new SAXReader();
        try(XMLStreamProvider.StreamHandle handle = stream.getStreamHandle()){

            Document document = reader.read(handle.getInputStream());

            List<Item> items = new ArrayList<>();

            Element channel = document.getRootElement().element("channel");

            for(Iterator<Element> elementIterator = channel.elementIterator("item"); elementIterator.hasNext();){

                Element element = elementIterator.next();

                String pubDate = element.element("pubDate").getText();
                Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(pubDate);

                items.add(new Item(
                        element.element("title").getText(),
                        StringUtils.defaultString(element.element("link").getText()).trim(),
                        StringUtils.defaultString(element.element("description").getText()).trim(),
                        date.getTime(),
                        StringUtils.defaultString(element.element("author").getText()).trim()
                ));
            }

            return items;
        }
        catch(Exception ex){
            Log.e(RSSMessageProvider.class.getSimpleName(), "Failed to connect to the stream", ex);
            throw new RuntimeError("Failed to read the stream");
        }
    }

    @Override
    public Post getPost(Item item) throws ApplicationError{

        String urlStr = item.getLink();
        String postBody = getPostBody(urlStr);

        return new Post(item, postBody);

    }

    /**
     *
     * @param urlStr
     * @return
     * @throws ApplicationError
     */
    @NonNull
    String getPostBody(String urlStr) throws ApplicationError {
        urlStr = urlStr.replaceAll("(/d/)", "/forum/print/");
        System.out.println(urlStr);
        HttpURLConnection connection =  null;

        String postBody;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            StringBuilder bld = new StringBuilder();
            try(InputStream inputStream = connection.getInputStream()){
                Scanner scanner = new Scanner(inputStream);
                while(scanner.hasNextLine()){
                    bld.append(scanner.nextLine()).append("\n");
                }

                postBody = bld.toString();
            }

            //  Now we parse
            org.jsoup.nodes.Document document = Jsoup.parse(postBody);
            document.outputSettings().prettyPrint(false);
            document.select("div").append("\\n");
            document.select("br").append("\\n");

            org.jsoup.nodes.Element element = document.getElementsByClass("messageContent").get(0);

            String html = element.html().replaceAll("\\\\n", "\n");
            postBody = Jsoup.clean(html, "", Whitelist.none(), new org.jsoup.nodes.Document.OutputSettings().prettyPrint(false));
            postBody = postBody.trim();

        }
        catch(Exception ex){
            Log.e(RSSMessageProvider.class.getSimpleName(), "Failed to fetch message content", ex);
            throw new ApplicationError("Failed to fetch message contents");
        }
        finally{
            if(connection != null) {
                connection.disconnect();
            }
        }
        return postBody;
    }
}
