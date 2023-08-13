package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserRegistrationRequest;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> register(UserRegistrationRequest registrationRequest) {
        // Check if User already exists
        Map<String, String> errors = new HashMap<>();

        Optional<User> existingUsername = userRepository.findByUsername(registrationRequest.getUsername());
        if(existingUsername.isPresent()){
            errors.put("username", "Username already taken.");
        }

        if (!errors.isEmpty()){
            return ResponseEntity.badRequest().body(errors);
        }

        // Hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        // Store the user in the database
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


}
