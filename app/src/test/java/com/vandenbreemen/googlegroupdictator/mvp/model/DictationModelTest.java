package com.vandenbreemen.googlegroupdictator.mvp.model;

import com.vandenbreemen.googlegroupdictator.post.api.Item;
import com.vandenbreemen.googlegroupdictator.post.api.Post;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by kevin on 07/01/18.
 */
public class DictationModelTest {

    private static final Item testItem = new Item(
            "Test Post",
            "nolin",
            "Post for unit Testing",
            1123l,
            ""
    );

    private static class TestPost extends Post {
        public TestPost(String body) {
            super(testItem, body);
        }
    }

    private String nbspPost;

    @Before
    public void setup(){
        try{
            Scanner input = new Scanner(new FileInputStream("testResource/parseTest1"));
            StringBuilder bld = new StringBuilder();

            while(input.hasNextLine()){
                bld.append(input.nextLine()).append("\n");
            }
            this.nbspPost = bld.toString();
            input.close();


        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Failed to load data for test");
        }
    }

    @Test
    public void sanityTestParseOutUtterances(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        assertFalse("Utterance list expected", CollectionUtils.isEmpty(model.getUtterances()));

        System.out.println(model.getUtterances());
    }

    @Test
    public void testRemoveNonBreakingSpaces(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        List<String> utterances = model.getUtterances();

        utterances.forEach(utterance->{
            assertFalse("Utterance should not contain non-breaking space char but found\n'"+utterance+"'",
                    utterance.contains("&nbsp;"));
        });
    }

    @Test
    public void testRemoveHtmlGTandLT(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        List<String> utterances = model.getUtterances();

        utterances.forEach(utterance-> {
            assertFalse("Utterance should not contain gt or lt char but found\n'" + utterance + "'",
                    utterance.contains("<") || utterance.contains(">"));
        });
    }

    @Test
    public void testProducesNoEmptyUtterances(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        List<String> utterances = model.getUtterances();
        utterances.forEach(utterance->assertFalse("Should contain no empty utterances", StringUtils.isBlank(utterance)));
    }


    //  Don't allow ellipses as these can mess up dictation
    @Test
    public void testProducesNoEllipses(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        List<String> utterances = model.getUtterances();
        utterances.forEach(utterance->assertFalse("Unexpected ellipses found - \n"+utterance,
                utterance.contains("...") || utterance.contains("..")
                ));
    }

    @Test
    public void testParseOutURLs(){
        Post post = new TestPost(nbspPost);
        DictationModel model = new DictationModel(Arrays.asList(post));

        List<String> utterances = model.getUtterances();
        AtomicBoolean found = new AtomicBoolean(false);
        utterances.forEach(uterance->{
            if(uterance.startsWith("this is a website")){
                assertEquals("URL replace",
                        "this is a website URL url expected Besides, Seven said in a log entry in \"Hope and Fear\" that Quantum Slipstream technology was similar to the transwarp drive used by the Borg",
                        uterance
                        );
                found.set(true);
            }
        });

        assertTrue("Should have encountered line", found.get());

    }

}
