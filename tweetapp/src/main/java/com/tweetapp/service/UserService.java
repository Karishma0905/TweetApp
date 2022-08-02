package com.tweetapp.service;

import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;

import java.util.List;
import java.util.Map;

public interface UserService {

    User createUser(User user) throws UsernameAlreadyExistsException;
    UserResponse login(String username, String password) throws InvalidUsernameOrPasswordException;
    String forgotPassword(String username);
    String resetPassword(String username, String password);
    List<User> getAllUsers();
    List<User> searchByUsername(String username) throws InvalidUsernameOrPasswordException;
}
