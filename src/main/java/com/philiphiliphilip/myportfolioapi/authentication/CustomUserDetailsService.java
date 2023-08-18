package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.model.User;
import com.philiphiliphilip.myportfolioapi.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        // User in this case is Spring Securitys 'User' class.
        // Behind the scenes, Spring Security will compare the user.getPassword() against the password that the
        // user provided at /login through the UserDetails object. This is done with the help of BCryptPasswordEncoder that we
        // made as @Bean in SecurityConfiguration.
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), Collections.emptyList());

    }
}
