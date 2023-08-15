package com.philiphiliphilip.myportfolioapi.authentication;

import com.philiphiliphilip.myportfolioapi.User.UserLoginRequest;
import com.philiphiliphilip.myportfolioapi.User.UserLoginResponse;
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

    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;


    public JwtService(AuthenticationManager authenticationManager,
                      JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        // Create an authentication token
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());

        // Authenticate the user. This block of code will delegate the authentication process to one or more
        // AuthenticationProvider instances, which will use my own written CustomUserDetailsService to load the UserDetails
        // by the provided username from the db. Once fetched, the AuthenticationProvider will compare the passwords
        // from UserDetails with the password provided in the UserLoginRequest, using the given encoder (BCrypto) to
        // verify the match. If successful, it will return true and the Authentication object will contain the user's details
        // and granted authorities.
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        String token = createToken(authenticatedUser);
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

