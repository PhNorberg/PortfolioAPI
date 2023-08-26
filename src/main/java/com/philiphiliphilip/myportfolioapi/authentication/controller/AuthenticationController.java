package com.philiphiliphilip.myportfolioapi.authentication.controller;

import com.philiphiliphilip.myportfolioapi.user.request.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.user.response.UserLoginResponse;
import com.philiphiliphilip.myportfolioapi.user.request.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.user.response.UserRegistrationResponse;
import com.philiphiliphilip.myportfolioapi.authentication.service.AuthenticationService;
import com.philiphiliphilip.myportfolioapi.authentication.service.JwtService;
import com.philiphiliphilip.myportfolioapi.exception.InvalidLoginFormException;
import com.philiphiliphilip.myportfolioapi.exception.InvalidRegistrationFormException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private AuthenticationService authService;
    private JwtService jwtService;

    public AuthenticationController(AuthenticationService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService =jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest,
                                                             BindingResult bindingResult){

        logUserAction("register", userRegistrationRequest.getUsername());

        if(bindingResult.hasErrors()){
            throw new InvalidRegistrationFormException(bindingResult);
        }

        UserRegistrationResponse userRegistrationResponse = authService.register(userRegistrationRequest, bindingResult);
        return ResponseEntity.ok(userRegistrationResponse);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                                   BindingResult bindingResult){


        logUserAction("login", userLoginRequest.getUsername());

        if(bindingResult.hasErrors()){
            throw new InvalidLoginFormException(bindingResult);
        }

        UserLoginResponse userLoginResponse = jwtService.login(userLoginRequest);
        return ResponseEntity.ok(userLoginResponse);
    }

    private void logUserAction(String action, String username){
        log.debug("User tries to {} with username {}.", action, username);
    }
}
