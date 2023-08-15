package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest, BindingResult bindingResult){

        // Log user attempt to register
        log.debug("User tries to register with username {}", registrationRequest.getUsername());

        // Check if valid registration request
        if(bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, error -> Objects.toString(error.getDefaultMessage(), "")));
            // Log unsuccessful register attempt.
            log.debug("User with username {} failed to register due to validation errors: {}", registrationRequest.getUsername(), errors);
            return ResponseEntity.badRequest().body(errors);
        }
        // Else go to service layer
        UserRegistrationResponse userRegistrationResponse = authService.register(registrationRequest, bindingResult);
        return ResponseEntity.ok(userRegistrationResponse);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest){
        return jwtService.login(userLoginRequest);
    }

}
