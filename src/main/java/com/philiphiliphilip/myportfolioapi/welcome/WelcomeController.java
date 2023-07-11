package com.philiphiliphilip.myportfolioapi.welcome;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping(path = "/")
    public WelcomeBean helloWorld(){
        return new WelcomeBean("Welcome to this portfolio API, which lets you track your portfolio performance versus a " +
                "wide variety of assets. Check these links out: " +
                "-> To be added");
    }

//    @GetMapping("/me")
//    public Authentication me(Authentication authentication) {
//        return authentication;
//    }
}
