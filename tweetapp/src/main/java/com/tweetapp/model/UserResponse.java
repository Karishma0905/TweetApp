package com.tweetapp.model;

public class UserResponse {
    private User user;
    private String loginStatus;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

   
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

	public UserResponse(User user, String loginStatus, String token) {
		this.user = user;
		this.loginStatus = loginStatus;
		this.token = token;
	}

	public UserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
    
}
