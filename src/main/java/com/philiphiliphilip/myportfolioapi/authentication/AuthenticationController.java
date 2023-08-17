package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.User.UserLoginResponse;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest, BindingResult bindingResult){

        // Lower-case the username and then capitalize the first letter
        String username = userRegistrationRequest.getUsername().toLowerCase();
        userRegistrationRequest.setUsername(StringUtils.capitalize(username));

        // Log user attempt to register
        logUserAction("register", userRegistrationRequest.getUsername());

        // Check if valid registration request
        if(bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            // Log unsuccessful register attempt.
            logUnsuccessfulUserAction("register", userRegistrationRequest.getUsername(), errors);
            return ResponseEntity.badRequest().body(errors);
        }
        // Else go to service layer
        UserRegistrationResponse userRegistrationResponse = authService.register(userRegistrationRequest, bindingResult);
        return ResponseEntity.ok(userRegistrationResponse);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult bindingResult){

        // Lower-case the username and then capitalize the first letter
        String username = userLoginRequest.getUsername().toLowerCase();
        userLoginRequest.setUsername(StringUtils.capitalize(username));

        // Log user attempt to login
        logUserAction("login", userLoginRequest.getUsername());

        // Check if valid login request
        if(bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            // Log unsuccessful login attempt.
            logUnsuccessfulUserAction("login", userLoginRequest.getUsername(), errors);
            return ResponseEntity.badRequest().body(errors);
        }
        // Else go to service layer
        UserLoginResponse userLoginResponse = jwtService.login(userLoginRequest);
        return ResponseEntity.ok(userLoginResponse);
    }

    private Map<String, String> getErrors(BindingResult bindingResult){
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> Objects.toString(error.getDefaultMessage(), "")));
    }

    private void logUnsuccessfulUserAction(String action, String username, Map<String, String> errors){
        log.debug("User with username {} failed to {} due to validation errors: {}", username, action, errors);
    }
    private void logUserAction(String action, String username){
        log.debug("User tries to {} with username {}.", action, username);
    }
}
