package com.philiphiliphilip.myportfolioapi.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

        // Get info about user for loggings sake
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("GET /users was called by user {}.", username);
        List<UserDTOUsersLevel> users = userService.getAllUsers();
        log.debug("GET /users call was successful by user {}.", username);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTOUsernameLevel> retrieveUser(@PathVariable String username){

        // Get info about user for loggings sake
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callingUser = authentication.getName();
        log.debug("GET /users/{} was called by user {}.", username, callingUser);
        UserDTOUsernameLevel userDTOUsernameLevel = userService.retrieveUserByUsername(username);
        log.debug("GET /users/{} call was successful by user {}.", username, callingUser);

        return ResponseEntity.ok(userDTOUsernameLevel);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }
}
