package com.philiphiliphilip.myportfolioapi.User;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTOUsersLevel>> retrieveAllUsers(){

        /// Get username of caller for loggings sake
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("GET /users was called by user {}.", callerUsername);
        List<UserDTOUsersLevel> users = userService.getAllUsers();
        log.debug("GET /users call was successful by user {}.", callerUsername);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTOUsernameLevel> retrieveUser(@PathVariable String username){

        // Get username of caller for loggings sake
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("GET /users/{} was called by user {}.", username, callerUsername);
        UserDTOUsernameLevel userDTOUsernameLevel = userService.retrieveUserByUsername(username);
        log.debug("GET /users/{} call was successful by user {}.", username, callerUsername);

        return ResponseEntity.ok(userDTOUsernameLevel);
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("@usernameTransformer.transform(#username) == authentication.name")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable String username){


        // Get username of caller for loggings sake
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("DELETE /users/{} was called by user {}.", username, callerUsername);
        UserDeleteResponse userDeleteResponse = userService.deleteUser(callerUsername);
        return ResponseEntity.ok(userDeleteResponse);
    }
}
