package com.tweetapp;

import com.tweetapp.exception.IncorrectOrDeletedTweet;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.service.TweetServiceImpl;
import com.tweetapp.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    TweetRepository tweetRepo;

    @InjectMocks
    TweetServiceImpl tweetService;

    @InjectMocks
    UserServiceImpl userService;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }

    private User user1;
    private User user2;
    private Tweet tweet1;
    private Tweet tweet2;
    private Tweet tweet3;
    private Tweet tweet4;

    @BeforeEach
    public void setup(){
        user1 = new User("karishma@09", "Karishma@0905", "karishma09@gmail.com", "Karishma", "Mohammad", "7845632678");
        user2 = new User("ayaan@765", "Ayaan@123", "ayaan@gmail.com", "Ayaan", "Mohammad", "7645238976");
        tweet1 = new Tweet("1","App", LocalDateTime.now(),123L, new User( "sherin@17","Sherin@1705","sherin1705@fmail.com","Sherin",
                "Mahammad","7638274561"), new ArrayList<>(),"Monsoon");
        tweet2 = new Tweet("2","Flower", LocalDateTime.now(),125L, new User("lalith","Lalith@987","lalith987@fmail.com","Lalith",
                "Kumar","9782457631"), new ArrayList<>(),"Monsoon");
        tweet4 = new Tweet("2","Flower", LocalDateTime.now(),125L, new User("lalith","Lalith@987","lalith987@fmail.com","Lalith",
                "Kumar","9782457631"), new ArrayList<>(),"Monsoon");
        }
    
    /*
     * TEST GET ALL TWEETS
     */
    @Test
    @DisplayName("Test getAllTweets() for service")
    public void testGetAllTweets(){
        when(tweetRepo.findAll()).thenReturn(Arrays.asList(tweet1));
        assertThat(tweetService.getAllTweets()).hasSize(1);
    }

    /*
     * TEST VALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test getAllTweetsByUsername() for service")
    public void testGetAllTweetsByUsername() {
        when(tweetRepo.findByUserUsername("sherin@17")).thenReturn(Arrays.asList(tweet1));
        assertTrue(tweetService.getAllTweetsByUsername((String) "sherin@17").equals(Arrays.asList(tweet1)));
        verify(tweetRepo).findByUserUsername("sherin@17");
    }

    /*
     * TEST INVALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test Invalid getAllTweetsByUsername() for service")
    public void testInvalidGetAllTweetsByUsername() throws Exception{
        when(tweetRepo.findByUserUsername("abc@12")).thenReturn(null);
        assertThrows(InvalidUsernameOrPasswordException.class, () -> { tweetService.getAllTweetsByUsername("abc@12");
        });
        verify(tweetRepo).findByUserUsername((String) "abc@12");

    }

    /*
     * ADD NEW TWEET
     */
    @Test
    @DisplayName("Test addNewTweets() for service")
    public void testAddNewTweet(){
        tweet3 = new Tweet( "4","Rain", LocalDateTime.now(),145L, user2, new ArrayList<>(),"Floods");
        when(tweetRepo.save(tweet3)).thenReturn(tweet3);
        assertTrue(tweetService.postNewTweet(tweet3).equals(tweet3));
        verify(tweetRepo).save(tweet3);
    }

    /*
     * TEST VALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test updateTweet() of TweetService")
    public void testUpdateTweet(){
        when(tweetRepo.findById(tweet1.getId())).thenReturn(Optional.of(tweet1));
        when(tweetRepo.save(tweet1)).thenReturn(tweet1);
        assertTrue(tweetService.updateTweetById(tweet1.getId(),tweet1).equals(tweet1));
        verify(tweetRepo).findById(tweet1.getId());
    }

    /*
     * TEST INVALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test Invalid updateTweet() of TweetService")
    public void testInvalidUpdateTweet() throws IncorrectOrDeletedTweet {
        when(tweetRepo.findById("99")).thenReturn(Optional.empty());
        assertThrows(IncorrectOrDeletedTweet.class, () -> { tweetService.updateTweetById("99", tweet4);
        });
        verify(tweetRepo).findById("99");
    }

    /*
     * TEST DELETE TWEET
     */
    @Test
    @DisplayName("Test deleteTweet() of TweetService")
    public void testDeleteTweet() {
        when(tweetRepo.findById("1")).thenReturn(Optional.of(tweet1));
        assertTrue(tweetService.deleteTweetById("1").equals("Tweet deleted successfully"));
        verify(tweetRepo).findById("1");
    }

    /*
     * TEST VALID LIKE TWEET
     */
    @Test
    @DisplayName("Test likeTweet() of TweetService")
    public void testLikeTweet() {
        when(tweetRepo.findById("1")).thenReturn(Optional.of(tweet1));
        assertTrue(tweetService.likeTweetById("1").equals("Liked Tweet successfully"));
        verify(tweetRepo).findById("1");
    }

    /*
     * TEST INVALID LIKE TWEET
     */
    @Test
    @DisplayName("Test Invalid LikeTweet() for TweetService")
    public void testInvalidLikeTweet() throws IncorrectOrDeletedTweet {
        when(tweetRepo.findById("4")).thenReturn(Optional.empty());
        assertThrows(IncorrectOrDeletedTweet.class,() -> { tweetService.likeTweetById("4");
        });
        verify(tweetRepo).findById("4");

    }
    
    /*
     * TEST REPLY TWEET
     */
    @Test
    @DisplayName("Test replyTweet() of TweetService")
    public void testReplyTweet(){
        when(tweetRepo.findById("1")).thenReturn(Optional.empty());
        assertTrue(tweetService.replyTweetById("Hi","1").equals("Replied tweet successfully"));
        verify(tweetRepo).findById("1");
    }



}
