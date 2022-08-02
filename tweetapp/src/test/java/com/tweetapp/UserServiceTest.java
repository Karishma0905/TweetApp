package com.tweetapp;

import com.tweetapp.exception.IncorrectOrDeletedTweet;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TokenService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    TweetServiceImpl tweetService;
    
    @Mock
    TokenService tokenService;

    @Mock
    UserRepository userRepo;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private User user1;
    private User user2;
    private User user3;
    private UserResponse response;


    @BeforeEach
    public void setup() {
        user1 = new User( "karishma@09", "Karishma@0905", "karishma09@gmail.com", "Karishma", "Mohammad", "7845632678");
        response = new UserResponse(user1,"active","jhsdi");
        

    }
    
    /*
     * TEST VALID REGISTER USER
     */
    @Test
    @DisplayName("Test valid createUser() of UserService")
    public void testCreateUser() {
    	when(userRepo.save(user1)).thenReturn(user1);
    	assertTrue(userService.createUser(user1).equals(user1));
    	verify(userRepo).save(user1);
    }
    
    /*
     * TEST INVALID REGISTER USER
     */
    @Test
    @DisplayName("Test Invalid createUser() of UserService")
    public void testInvalidCreateUser() throws Exception {
    	user2 = new User("karishma@09", "Ayaan@123", "ayaan@gmail.com", "Ayaan", "Mohammad", "7645238976");
    	when(userRepo.findByUsername("karishma@09")).thenReturn(user1);
    	assertThrows(UsernameAlreadyExistsException.class, () -> {
    		userService.createUser(user2);
    	});
    	verify(userRepo).findByUsername("karishma@09");
    }

    /*
     * TEST VALID LOGIN
     */
    @Test
    @DisplayName("Test valid login() of UserService")
    public void testlogin() {
    	when(userRepo.findByUsername("karishma@09")).thenReturn(user1);
    	when(tokenService.createToken(user1.getUsername())).thenReturn("jhsdi");
    	assertThat(userService.login("karishma@09", "Karishma@0905").getUser().getUsername()).isEqualTo(response.getUser().getUsername());
    	assertThat(userService.login("karishma@09", "Karishma@0905").getToken()).isEqualTo(response.getToken());
    	verify(userRepo, times(2)).findByUsername("karishma@09");
    	verify(tokenService,times(2)).createToken(user1.getUsername());
    	}
    
    /*
     * TEST INVALID LOGIN FOR USERNAME
     */
    @Test
    @DisplayName("Test Invalid username login() of userService")
    public void testInvalidUsernameLogin() throws Exception {
    	when(userRepo.findByUsername("kousar")).thenReturn(null);
    	assertThrows(InvalidUsernameOrPasswordException.class,() -> {
    		userService.login("kousar", "kousar");
    	});
    	verify(userRepo).findByUsername("kousar");
    }
    
    /*
     * TEST INVALID LOGIN FOR PASSWORD
     */
    @Test
    @DisplayName("Test Invalid password login() of userService")
    public void testInvalidPasswordLogin() throws Exception {
    	when(userRepo.findByUsername("kousar")).thenReturn(user1);
    	when(tokenService.createToken("user1")).thenReturn(null);
    	assertThrows(InvalidUsernameOrPasswordException.class,() -> {
    		userService.login("kousar", "kousar");
    	});
    	verify(userRepo).findByUsername("kousar");
    }
    
    /*
     * TEST FORGOT PASSWORD
     */
    @Test
    @DisplayName("Test forgotPassword() of userservice")
    public void testForgotPassword() {
    	when(userRepo.findByUsername("karishma@09")).thenReturn(user1);
    	assertTrue(userService.forgotPassword("karishma@09").equals("forgot password"));
    	verify(userRepo).findByUsername("karishma@09");
    }
    
    /*
     * TEST RESET PASSWORD
     */
    @Test
    @DisplayName("Test resetPassword() of userservice")
    public void testResetPassword() {
    	when(userRepo.findByUsername("karishma@09")).thenReturn(user1);
    	assertTrue(userService.resetPassword("karishma@09", "karishma@0905").equals("reset password"));
    	verify(userRepo).findByUsername("karishma@09");
    }
   
    /*
     * TEST GET ALL USERS
     */
    @Test
    @DisplayName("Test getAllUsers() of UserService")
    public void testGetAllUsers(){
        when(userRepo.findAll()).thenReturn(Arrays.asList(user1));
        assertThat(userService.getAllUsers()).hasSize(1);
    }

    /*
     * TEST VALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test searchByUsername() of UserService")
    public void testSearchByUsername(){
        when(userRepo.findByUsernameContaining("karishma@09")).thenReturn(List.of(user1));
        assertTrue(userService.searchByUsername("karishma@09").equals(Arrays.asList(user1)));
        verify(userRepo,times(2)).findByUsernameContaining("karishma@09");
    }

    /*
     * TEST INVALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test InvalidSearchByUsername() of UserService")
    public void testInvalidSearchByUsername() throws Exception{
        when(userRepo.findByUsernameContaining("sherin@12")).thenReturn(null);
        assertThrows(InvalidUsernameOrPasswordException.class, () -> { userService.searchByUsername((String) "sherin@12");
        });
        verify(userRepo).findByUsernameContaining((String) "sherin@12");
    }
}



