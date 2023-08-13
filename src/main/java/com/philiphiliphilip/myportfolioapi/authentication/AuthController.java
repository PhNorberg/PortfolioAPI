package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthService authService;
    private JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService =jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest registrationRequest){
        return authService.register(registrationRequest);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest){
        return jwtService.login(userLoginRequest);
    }

}
