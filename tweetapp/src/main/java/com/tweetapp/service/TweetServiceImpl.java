package com.tweetapp.service;


import com.tweetapp.exception.IncorrectOrDeletedTweet;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.kafka.ProducerService;
//import com.tweetapp.kafka.ProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService{
    @Autowired
    TweetRepository tweetRepository;
    

    @Autowired
    ProducerService producerService;

    Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);

    @Override
    public List<Tweet> getAllTweets(){
        logger.info("Getting all the tweets");
        List<Tweet> tweet;
        tweet = tweetRepository.findAll();
        logger.info("Getting all tweets");
        return tweet;
    }

    @Override
    public List<Tweet> getAllTweetsByUsername(String  username) throws InvalidUsernameOrPasswordException
    {
    	List<Tweet> allTweets = tweetRepository.findByUserUsername(username);
    	if(allTweets != null) {
    		logger.info("Getting all tweets by username" +username);
    		return allTweets;
    		
    	}
    	else {
    		throw new InvalidUsernameOrPasswordException("Invalid credentials");
    	}
        
         
    }

    @Override
    public Tweet postNewTweet(Tweet tweet)
    {
        logger.info("New tweet posted successfully");
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet updateTweetById(String id, Tweet tweet) throws IncorrectOrDeletedTweet
    {
        Tweet updateTweet = tweetRepository.findById(id).orElse(null);
        if(updateTweet!=null)
        {
            
            tweet = tweetRepository.save(updateTweet);
            producerService.sendMessage(LocalDateTime.now() + "-" + "updated Tweet" + "-" + tweet.getUser().getUsername());
            logger.info("Updated tweet successfully");
            return tweet;
        }
        else 
        	logger.info("Tweet not updated");
        	throw new IncorrectOrDeletedTweet("Incorrect or deleted tweet");
       
    }

    @Override
    public String deleteTweetById(String id)
    {
        Tweet deleteTweet = tweetRepository.findById(id).orElse(null);
        if(deleteTweet!=null)
        {
            logger.info("Tweet Deleted Successfully");
            tweetRepository.delete(deleteTweet);
            
        }
        return "Tweet deleted successfully";


    }

    @Override
    public String likeTweetById(String id) throws IncorrectOrDeletedTweet
    {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if(tweet.isPresent()) {
            tweet.get().setLikes(tweet.get().getLikes() + 1);
            tweetRepository.save(tweet.get());
            return "Liked Tweet successfully";
        }
        else 
        	throw new IncorrectOrDeletedTweet("Incorrect or deleted tweet");
        
    }

    @Override
    public String replyTweetById(String replyTweet, String id) 
    {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if(tweet.isPresent())
        {
            List<String> replies = tweet.get().getReplies();
            replies.add(replyTweet);
            tweet.get().setReplies(replies);
            tweetRepository.save(tweet.get());
        }
        return "Replied tweet successfully" ;
    }

}
