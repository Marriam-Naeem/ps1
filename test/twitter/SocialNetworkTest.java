/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
	
	private static final Instant TIME1 = Instant.parse("2019-05-20T10:00:00Z");
	private static final Instant TIME2 = Instant.parse("2019-05-20T12:00:00Z");
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphWithMentions() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "alice", "@bob Hello!",TIME1));
        tweets.add(new Tweet(2, "bob", "Hi, @alice!",TIME2));
        
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue(followsGraph.containsKey("alice"));
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.containsKey("bob"));
        assertTrue(followsGraph.get("bob").contains("alice"));
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "alice", "Hello, world!",TIME1));
        tweets.add(new Tweet(2, "bob", "Hi there!",TIME2));

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("Expected graph to contain 'alice'", followsGraph.containsKey("alice"));
        assertTrue("Expected graph to contain 'bob'", followsGraph.containsKey("bob"));
        assertEquals("Expected 'alice' to have no followers", 0, followsGraph.get("alice").size());
        assertEquals("Expected 'bob' to have no followers", 0, followsGraph.get("bob").size());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersWithFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", Set.of("bob", "carol", "dave"));
        followsGraph.put("bob", Set.of("carol", "dave"));
        followsGraph.put("carol", Set.of("dave","alice"));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Expected order: dave, carol, bob, alice
        assertEquals("Expected 4 influencers", 4, influencers.size());
        assertEquals("dave", influencers.get(0));
        assertEquals("carol", influencers.get(1));
        assertEquals("bob", influencers.get(2));
        assertEquals("alice", influencers.get(3));
    }
    
    @Test
    public void testInfluencersNoInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", Set.of("bob"));
        followsGraph.put("bob", Set.of("alice"));
        followsGraph.put("carol", Set.of("dave"));
        followsGraph.put("dave", Set.of("carol"));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // No one is an influencer
        assertFalse("Expected non-empty list", influencers.isEmpty());
        assertEquals("Expected 4 users in the list", 4, influencers.size());
    }


    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
