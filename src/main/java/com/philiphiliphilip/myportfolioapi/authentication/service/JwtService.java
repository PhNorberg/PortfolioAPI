package com.philiphiliphilip.myportfolioapi.authentication.service;

import com.philiphiliphilip.myportfolioapi.user.request.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.user.response.UserLoginResponse;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final static Logger log = LoggerFactory.getLogger(JwtService.class);
    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private NameFormatter usernameFormatter;

    public JwtService(AuthenticationManager authenticationManager,
                      JwtEncoder jwtEncoder,
                      @Qualifier("usernameFormatter") NameFormatter usernameFormatter) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.usernameFormatter = usernameFormatter;
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        // Format username
        String capitalizedUsername = usernameFormatter.format(userLoginRequest.getUsername());
        // Create an authentication token
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(capitalizedUsername, userLoginRequest.getPassword());

        // Authenticate the user. This block of code will delegate the authentication process to one or more
        // AuthenticationProvider instances, which will use my own written CustomUserDetailsService to load the UserDetails
        // by the provided username from the db. Once fetched, the AuthenticationProvider will compare the passwords
        // from UserDetails with the password provided in the UserLoginRequest, using the given encoder (BCrypto) to
        // verify the match. If successful, it will return true and the Authentication object will contain the user's details
        // and granted authorities.
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        String token = createToken(authenticatedUser);
        log.debug("User with username {} logged in successfully.", capitalizedUsername);
        return new UserLoginResponse(token);
    }
    
    private String createToken(Authentication authentication){
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 60))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(" "));
    }

}

