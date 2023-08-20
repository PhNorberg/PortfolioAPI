package com.philiphiliphilip.myportfolioapi.authentication.service;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.user.request.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.user.response.UserRegistrationResponse;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.exception.UsernameAlreadyExistsException;
//import jakarta.transaction.Transactional;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private NameFormatter usernameFormatter;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 @Qualifier("usernameFormatter") NameFormatter usernameFormatter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameFormatter = usernameFormatter;
    }

    @Transactional
    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest, BindingResult bindingResult) {

        // Check if User with this username already exists
        String capitalizedUsername = usernameFormatter.format(registrationRequest.getUsername());
        Optional<User> existingUsername = userRepository.findByUsername(capitalizedUsername);
        if(existingUsername.isPresent()){
            throw new UsernameAlreadyExistsException(capitalizedUsername);
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        // Store the user in the database
        User newUser = new User(capitalizedUsername, hashedPassword);
        userRepository.save(newUser);
        // Log successful register attempt.
        log.debug("User with username {} registered successfully.", capitalizedUsername);
        return new UserRegistrationResponse();
    }
}
