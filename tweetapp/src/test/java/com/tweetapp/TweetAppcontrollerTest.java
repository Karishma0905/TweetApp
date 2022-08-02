package com.tweetapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.exception.IncorrectOrDeletedTweet;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.controller.TweetAppController;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TokenService;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;


@WebMvcTest(value = TweetAppController.class)
public class TweetAppcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    TweetService tweetService;

    @MockBean
    TokenService tokenSevice;

 
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    private User User1;
    private User User2;
    private Tweet tweet1;
    private Tweet tweet2;
    private Tweet tweet3;
    private UserResponse response;

    @BeforeEach
    public void setup(){
        User1 = new User("karishma@09", "Karishma@0905", "karishma09@gmail.com", "Karishma", "Mohammad", "7845632678");
        User2 = new User( "sherin@17","Sherin@1705","sherin1705@fmail.com","Sherin","Mahammad","7638274561");
        tweet1 = new Tweet("1","App", LocalDateTime.now(),123L, new User ("sherin@17","Sherin@1705","sherin1705@fmail.com","Sherin",
                "Mahammad","7638274561"), new ArrayList<>(),"Monsoon");
        tweet2 = new Tweet("2","Flower", LocalDateTime.now(),125L, new User( "lalith","Lalith@987","lalith987@fmail.com","Lalith",
                "Kumar","9782457631"), new ArrayList<>(),"Monsoon");
        response = new UserResponse(User1,"active","jhsdi");
    }
    
    /*
     * TEST VALID REGISTER USER
     */

    @Test
    @DisplayName("Test valid registerUser()")
    public void testValidRegisterUser() throws Exception{
        when(userService.createUser(any())).thenReturn(User1);
        this.mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User1))).andExpect(status().isCreated());
        verify(userService, times(1)).createUser(any());
    }
    
    /*
     * TEST INVALID REGISTER USER
     */

    @Test
    @DisplayName("Test Invalid registerUser()")
    public void testInvalidRegisterUser() throws Exception{
        when(userService.createUser(any())).thenThrow(new UsernameAlreadyExistsException("Register with different username"));
        this.mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User2))).andExpect(status().isBadRequest());
        verify(userService, times(1)).createUser(any());
    }

    /*
     * TEST VALID LOGIN
     */
    @Test
    @DisplayName("Test valid Login()")
    public void testValidLogin() throws Exception{
        when(userService.login(any(),any())).thenReturn(response);
        this.mockMvc.perform(get("/api/v1.0/tweets/login").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User1))).andExpect(status().isOk());
        verify(userService,times(1)).login(any(),any());

    }

    /*
     * TEST INVALID LOGIN
     */
    @Test
    @DisplayName("Test Invalid Login()")
    public void testInvalidLogin() throws Exception{
        when(userService.login(any(), any())).thenThrow(new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS"));
        this.mockMvc.perform(get("/api/v1.0/tweets/login").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(null))).andExpect(status().isBadRequest());
        verify(userService,times(1)).login(any(), any());
    }

    /*
     * TEST FORGOT PASSWORD
     */
    @Test
    @DisplayName("Test forgotPassword()")
    public void testForgotPassword() throws Exception {
        when(userService.forgotPassword("karishma@09")).thenReturn("Reset password successfully");
        this.mockMvc.perform(get("/api/v1.0/tweets/karishma@09/forgot")).andExpect(status().isOk());
        verify(userService,times(1)).forgotPassword("karishma@09");
    }
    
    /*
     * TEST VALIDATE TOKEN
     */
    @Test
    @DisplayName("Test validateToken()")
    public void testValidateToken() throws Exception{
    	when(tokenSevice.isTokenValid(any())).thenReturn(true);
    	this.mockMvc.perform(get("/api/v1.0/tweets/validate")).andExpect(status().isOk());
    }

    /*
     * TEST GET ALL USERS
     */
    @Test
    @DisplayName("Test getAllUsers()")
    public void testGetAllUsers() throws Exception{
        when(userService.getAllUsers()).thenReturn(Arrays.asList(User1));
        this.mockMvc.perform(get("/api/v1.0/tweets/users/all")).andExpect(status().isOk());
        verify(userService,times(1)).getAllUsers();
    }
  
    /*
     * TEST VALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test  valid searchByUserName()")
    public void testValidSearchByUserName() throws Exception{
        when(userService.searchByUsername(any())).thenReturn(Arrays.asList(User1));
        this.mockMvc.perform(get("/api/v1.0/tweets/user/search/karishma@09")).andExpect(status().isOk());
        verify(userService,times(1)).searchByUsername(any());
    }
    
    /*
     * TEST INVALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test  Invalid searchByUserName()")
    public void testInvalidSearchByUserName() throws Exception{
        when(userService.searchByUsername("2000")).thenThrow(new InvalidUsernameOrPasswordException("Username not found"));
        this.mockMvc.perform(get("/api/v1.0/tweets/user/search/2000")).andExpect(status().isBadRequest());
        verify(userService,times(1)).searchByUsername("2000");
    }
  
    /*
     * TEST GET ALL TWEETS
     */
    @Test
    @DisplayName("Test getAllTweets()")
    public void testGetAllTweets() throws Exception {
        when(tweetService.getAllTweets()).thenReturn(Arrays.asList(tweet1));
        this.mockMvc.perform(get("/api/v1.0/tweets/all")).andExpect(status().isOk());
        verify(tweetService,times(1)).getAllTweets();
    }
  
    /*
     * TEST VALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test valid getAllTweetsOfUser()")
    public void testValidGetAllTweetsOfUser() throws Exception {
        when(tweetService.getAllTweetsByUsername("sherin@17")).thenReturn(Arrays.asList(tweet1));
        this.mockMvc.perform(get("/api/v1.0/tweets/{username}","sherin@17")).andExpect(status().isOk());
        verify(tweetService,times(1)).getAllTweetsByUsername(any());
    }

    /*
     * TEST INVALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test Invalid getAllTweetsOfUser()")
    public void testInvalidGetAllTweetsOfUser() throws Exception {
        when(tweetService.getAllTweetsByUsername(any())).thenThrow(new InvalidUsernameOrPasswordException("Tweets not found for given username"));
        this.mockMvc.perform(get("/api/v1.0/tweets/Soumya@1")).andExpect(status().isBadRequest());
        verify(tweetService,times(1)).getAllTweetsByUsername("Soumya@1");
    }

    /*
     * ADD NEW TWEET
     */
    @Test
    @DisplayName("Test postTweet()")
    public void testValidPostTweet() throws Exception {
        when(tweetService.postNewTweet(tweet1)).thenReturn(tweet1);
        this.mockMvc.perform(post("/api/v1.0/tweets/{username}/add","sherin@17").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).postNewTweet(any());
    }

    /*
     * TEST VALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test valid updateTweet()")
    public void testValidUpdateTweet() throws Exception {
        when(tweetService.updateTweetById("12",tweet1)).thenReturn(tweet1);
        this.mockMvc.perform(put("/api/v1.0/tweets/{username}/update/{id}","sherin@17","1").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).updateTweetById(any(),any());
    }

    /*
     *  TEST INVALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test Invalid updateTweet()")
    public void testInvalidUpdateTweet() throws Exception {
        when(tweetService.updateTweetById(any(),any())).thenThrow(new IncorrectOrDeletedTweet("Tweet not updated with given id"));
        this.mockMvc.perform(put("/api/v1.0/tweets/Shyam/update/12")).andExpect(status().isBadRequest());
        verify(tweetService,times(1)).updateTweetById(any(),any());
    }

    /*
     * TEST DELETE TWEET
     */
    @Test
    @DisplayName("Test deleteTweet()")
    public void testValidDeleteTweet() throws Exception {
        when(tweetService.deleteTweetById(any())).thenReturn("Tweet Deleted Successfully");
        this.mockMvc.perform(delete("/api/v1.0/tweets/sherin@17/delete/1")).andExpect(status().isOk());
        verify(tweetService,times(1)).deleteTweetById(any());
    }

    /*
     * TEST VALID LIKE TWEET
     */
    @Test
    @DisplayName("Test valid likeTweet()")
    public void testValidLikeTweet() throws Exception {
        when(tweetService.likeTweetById(any())).thenReturn("Liked");
        this.mockMvc.perform(put("/api/v1.0/tweets/sherin@17/like/1")).andExpect(status().isOk());
        verify(tweetService,times(1)).likeTweetById(any());
    }
    
    /*
     * TEST INVALID LIKE TWEET
     */
    @Test
    @DisplayName("Test Invalid likeTweet()")
    public void testInvalidLikeTweet() throws Exception {
        when(tweetService.likeTweetById(any())).thenThrow(new IncorrectOrDeletedTweet("Tweet not deleted"));
        this.mockMvc.perform(put("/api/v1.0/tweets/Sai/like/15")).andExpect(status().isInternalServerError());
        verify(tweetService,times(1)).likeTweetById(any());
    }

    /*
     * TEST REPLY TWEET
     */
    @Test
    @DisplayName("Test replyTweet()")
    public void testValidReplyTweet() throws Exception {
        when(tweetService.replyTweetById(any(),any())).thenReturn("Replied tweet successfully");
        this.mockMvc.perform(post("/api/v1.0/tweets/sherin@17/reply/1").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).replyTweetById(any(),any());
    }

   

}
