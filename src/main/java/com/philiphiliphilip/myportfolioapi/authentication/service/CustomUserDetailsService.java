package com.philiphiliphilip.myportfolioapi.authentication.service;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        // User in this case is Spring Securitys 'User' class.
        // Behind the scenes, Spring Security will compare the user.getPassword() against the password that the
        // user provided at /login through the UserDetails object. This is done with the help of BCryptPasswordEncoder that we
        // made as @Bean in SecurityConfiguration.
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), Collections.emptyList());
    }
}
