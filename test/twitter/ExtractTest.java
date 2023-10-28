/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.

 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
public class ExtractTest {

    private static final Instant TIME1 = Instant.parse("2019-05-20T10:00:00Z");
    private static final Instant TIME2 = Instant.parse("2019-05-20T12:00:00Z");

    private static final Tweet TWEET1 = new Tweet(1, "user1", "Hello , how are you?", TIME1);
    private static final Tweet TWEET2 = new Tweet(2, "user2", "@user1 I'm good, thank you!", TIME2);

    @Test(expected=AssertionError.class)
    public void testAssertsEnabled() {
        assert false; // Ensure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(TWEET1, TWEET2));

        assertEquals("Expected start time", TIME1, timespan.getStart());
        assertEquals("Expected end time", TIME2, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(TWEET1));

        assertTrue("Expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Collections.singletonList(TWEET1));
        assertEquals("Start and end should be equal", TIME1, timespan.getStart());
        assertEquals("Start and end should be equal", TIME1, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersOneMention() {
        Tweet tweet = new Tweet(3, "user3", "Hey @user4, how's it going?", Instant.parse("2019-05-20T14:00:00Z"));
        Set<String> mentionedUsers = Extract.getMentionedUsers(Collections.singletonList(tweet));
        Set<String> expectedMentions = new HashSet<>(Collections.singletonList("user4"));
        assertEquals("Mentioned user should be in the set", expectedMentions, mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Tweet tweet1 = new Tweet(4, "user4", "Hi @user5!", Instant.parse("2019-05-20T15:00:00Z"));
        Tweet tweet2 = new Tweet(5, "user5", "Hello @user4!", Instant.parse("2019-05-20T16:00:00Z"));
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2));
        Set<String> expectedMentions = new HashSet<>(Arrays.asList("user4", "user5"));
        assertEquals("Mentioned users should be in the set", expectedMentions, mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersCaseInsensitivity() {
        Tweet tweet = new Tweet(6, "UserA", "Hi @userB!", Instant.parse("2019-05-20T17:00:00Z"));
        Set<String> mentionedUsers = Extract.getMentionedUsers(Collections.singletonList(tweet));
        Set<String> expectedMentions = new HashSet<>(Collections.singletonList("userb"));
        assertEquals("Mentioned user should be in the set", expectedMentions, mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersMentionPosition() {
        Tweet tweet = new Tweet(7, "alice", "@bob Hello!", Instant.parse("2019-05-20T18:00:00Z"));
        Set<String> mentionedUsers = Extract.getMentionedUsers(Collections.singletonList(tweet));
        Set<String> expectedMentions = new HashSet<>(Collections.singletonList("bob"));
        assertEquals("Mentioned user should be in the set", expectedMentions, mentionedUsers);
    }

    // Additional test cases...

}
