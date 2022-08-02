package com.tweetapp.service;

import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;

import ch.qos.logback.core.status.Status;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    TokenService tokenService;

    @Override
    public   User createUser(User user) throws UsernameAlreadyExistsException
    {
      logger.info("***Inside CreateUSer***");
      if(userRepository.findByUsername(user.getUsername()) != null) {
    	  throw new UsernameAlreadyExistsException("username already exists");
      }
      User savedUser = userRepository.save(user);
      logger.info("Registration Successfull");
      return savedUser;
    }
    
    @Override
    public UserResponse login(String username, String password) throws InvalidUsernameOrPasswordException{
    	logger.debug("Inside User Service -> loginUser()");
    	UserResponse response = new UserResponse();
    		User user = userRepository.findByUsername(username);
    		if(user!=null) {
    			if(user.getPassword().equals(password)) {
    				response.setUser(user);
    				response.setLoginStatus("success");
    				response.setToken(tokenService.createToken(user.getUsername()));
    				logger.info(user.getUsername()+"Logged in successfully");
    				return response;
    			}
    			else 
    				throw new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS");
    		}
    		else 
        		throw new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS");
    	}
    
    
    
    @Override
    public String forgotPassword(String username){
    	Map<String, String> map = new HashMap<String, String>();
    	User user = userRepository.findByUsername(username);
    	user.setPassword(UUID.randomUUID().toString());
    	userRepository.save(user);
    	map.put("newPassword", user.getPassword());
    	map.put("resetStatus", "success");
    	logger.info("Completed");
    	return "Forgot password" ;
    }

    
    @Override
    public String resetPassword(String username,String password){
    	Map<String, String> map = new HashMap<String, String>();
    	User user = userRepository.findByUsername(username);
    	user.setPassword(password);
    	userRepository.save(user);
    	map.put("newPassword", user.getPassword());
    	map.put("resetStatus", "success");
    	logger.info("Reset password successfully");
    	return "Reset password successfully";
    }

    @Override
    public List<User> getAllUsers()
    {
        logger.info("Getting all users");
        List <User> user;
        user=userRepository.findAll();
        return user;
    }

    @Override
    public List<User> searchByUsername(String username) throws InvalidUsernameOrPasswordException
    {
    	 
    	if(userRepository.findByUsernameContaining(username) == null) {
    		throw new InvalidUsernameOrPasswordException("Invalid credentials");
    	}
    	else {
    		List<User> user = userRepository.findByUsernameContaining(username);
    		logger.info("Retriving the user by username" +username);
    		return user;
    	}
    }
       
       





}
