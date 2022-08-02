package com.tweetapp.service;

import com.tweetapp.exception.IncorrectOrDeletedTweet;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;

import java.util.List;

public interface TweetService {
    List<Tweet> getAllTweets();
    List<Tweet> getAllTweetsByUsername(String username) throws InvalidUsernameOrPasswordException;
    Tweet postNewTweet(Tweet tweet);
    Tweet updateTweetById(String id, Tweet tweet) throws IncorrectOrDeletedTweet;
    String deleteTweetById(String id);
    String likeTweetById(String id) throws IncorrectOrDeletedTweet;
    String replyTweetById(String replyTweet,String id);

}
