package com.philiphiliphilip.myportfolioapi.portfolio;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PortfolioController {

    private PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @GetMapping("/users/{username}/portfolios")
    public ResponseEntity<List<PortfolioDTOUsernameLevel>> getAllPortfolios(@PathVariable String username) {
        List<PortfolioDTOUsernameLevel> portfolios = portfolioService.getAllPortfolios(username);
        return ResponseEntity.ok(portfolios);
    }
    
    @GetMapping("/users/{username}/portfolios/{portfolioname}")
    // If you look at your own portfolio, you get a more detailed view than if you check another ones.
    public ResponseEntity<PortfolioDTO> getUserPortfolioByPortfolioname(@PathVariable String username, @PathVariable String portfolioname){
        PortfolioDTO portfolio = portfolioService.getUserPortfolioByPortfolioname(username, portfolioname);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/users/{username}/portfolios")
    @PreAuthorize("@usernameTransformer.transform(#username) == authentication.name")
    public ResponseEntity<PortfolioCreationResponse> createPortfolio(@RequestBody PortfolioCreationRequest creationRequest
            , @PathVariable String username){
        PortfolioCreationResponse creationResponse = portfolioService.createPortfolio(creationRequest, username);
        return ResponseEntity.ok(creationResponse);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}")
    public void deletePortfolio(@PathVariable Integer userId, @PathVariable Integer portfolioId){
        portfolioService.deletePortfolio(portfolioId);
    }
}
