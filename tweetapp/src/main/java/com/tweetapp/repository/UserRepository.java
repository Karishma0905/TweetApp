package com.tweetapp.repository;

import com.tweetapp.model.User;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository <User, Long> {

    User findByUsername(String username);
    
    List<User> findByUsernameContaining(String username);

}
