/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;


import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: Define testing strategies for these methods here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant timestamp1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant timestamp2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "user1", "Is it reasonable to talk about a topic so much?", timestamp1);
    private static final Tweet tweet2 = new Tweet(2, "user2", "Topic talk in 30 minutes #hype", timestamp2);
    
    @Test(expected=AssertionError.class)
    public void testAssertEnabled() {
        assert false; // Ensure assertions are enabled with VM argument: -ea
    }
    
    
    @Test
    public void testFilterBySingleTweetAuthor() {
        List<Tweet> filteredTweets = Filter.writtenBy(Arrays.asList(tweet1), "user1");

        assertEquals("Expected a singleton list", 1, filteredTweets.size());
        assertTrue("Expected list to contain tweet", filteredTweets.contains(tweet1));
    }
    
    
    
    @Test
    public void testFilterByMultipleTweetsSingleResult() {
        List<Tweet> filteredTweets = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "user1");
        
        assertEquals("Expected a singleton list", 1, filteredTweets.size());
        assertTrue("Expected list to contain tweet", filteredTweets.contains(tweet1));
    }
    
    
    @Test
    public void testFilterByTimespanMultipleTweetsMultipleResults() {
        Instant startTime = Instant.parse("2016-02-17T09:00:00Z");
        Instant endTime = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> filteredTweets = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(startTime, endTime));
        
        assertFalse("Expected a non-empty list", filteredTweets.isEmpty());
        assertTrue("Expected list to contain all tweets", filteredTweets.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("Expected the same order", 0, filteredTweets.indexOf(tweet1));
    }
    
    @Test
    public void testFilterByContainingWords() {
        List<Tweet> filteredTweets = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("Expected a non-empty list", filteredTweets.isEmpty());
        assertTrue("Expected list to contain all tweets", filteredTweets.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("Expected the same order", 0, filteredTweets.indexOf(tweet1));
    }
    
    @Test
    public void testFilterByContainingNoMatch() {
        List<Tweet> filteredTweets = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("apple"));

        assertTrue("Expected an empty list", filteredTweets.isEmpty());
    }
    
    /*
     * Warning: All the tests written here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
