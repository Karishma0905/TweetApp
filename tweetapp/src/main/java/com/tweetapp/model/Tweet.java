package com.tweetapp.model;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Tweet")
public class Tweet {
    @Id
    private String id;
    private String tweetName;

    @CreatedDate
    private LocalDateTime postDate;
    private long likes;
    private User user;
    private List<String> replies;
    private String tweetTags;

    public Tweet(String id, String tweetName, LocalDateTime postDate, long likes, User user, List<String> replies, String tweetTags) {
        this.id = id;
        this.tweetName = tweetName;
        this.postDate = postDate;
        this.likes = likes;
        this.user = user;
        this.replies = replies;
        this.tweetTags = tweetTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTweetName() {
        return tweetName;
    }

    public void setTweetName(String tweetName) {
        this.tweetName = tweetName;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getReplies() {
        return replies;
    }

    public void setReplies(List<String> replies) {
        this.replies = replies;
    }

    public String getTweetTags() {
        return tweetTags;
    }

    public void setTweetTags(String tweetTags) {
        this.tweetTags = tweetTags;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", tweetName='" + tweetName + '\'' +
                ", postDate=" + postDate +
                ", likes=" + likes +
                ", user=" + user +
                ", replies=" + replies +
                ", tweetTags='" + tweetTags + '\'' +
                '}';
    }
}

