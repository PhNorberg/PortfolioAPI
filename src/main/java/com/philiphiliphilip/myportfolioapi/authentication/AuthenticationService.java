package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationResponse;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.exception.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest, BindingResult bindingResult) {

        // Check if User already exists
        Optional<User> existingUsername = userRepository.findByUsername(registrationRequest.getUsername());
        if(existingUsername.isPresent()){
            throw new UsernameAlreadyExistsException(registrationRequest.getUsername());
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        // Store the user in the database
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);
        // Log successful register attempt.
        log.debug("User with username {} registered successfully.", registrationRequest.getUsername());
        return new UserRegistrationResponse("User registered successfully");
    }


}
