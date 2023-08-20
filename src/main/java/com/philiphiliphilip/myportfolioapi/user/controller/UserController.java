package com.philiphiliphilip.myportfolioapi.user.controller;

import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsersLevel;
import com.philiphiliphilip.myportfolioapi.user.response.UserDeleteResponse;
import com.philiphiliphilip.myportfolioapi.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTOUsersLevel>> getAllUsers(){

        List<UserDTOUsersLevel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTOUsernameLevel> getUserByUsername(@PathVariable String username){

        UserDTOUsernameLevel userDTOUsernameLevel = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTOUsernameLevel);
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("@usernameFormatter.format(#username) == authentication.name")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable String username){

        UserDeleteResponse userDeleteResponse = userService.deleteUser(username);
        return ResponseEntity.ok(userDeleteResponse);
    }
}
