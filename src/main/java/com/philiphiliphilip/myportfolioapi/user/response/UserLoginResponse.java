package com.philiphiliphilip.myportfolioapi.user.response;

public class UserLoginResponse {

    private final String token;

    public UserLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
