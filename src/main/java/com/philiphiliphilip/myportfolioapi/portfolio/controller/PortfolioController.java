package com.philiphiliphilip.myportfolioapi.portfolio.controller;


import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTO;
import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.portfolio.request.PortfolioCreationRequest;
import com.philiphiliphilip.myportfolioapi.portfolio.response.PortfolioCreationResponse;
import com.philiphiliphilip.myportfolioapi.portfolio.response.PortfolioDeletionResponse;
import com.philiphiliphilip.myportfolioapi.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<PortfolioDTOUsernameLevel>> getUsersPortfolios(@PathVariable String username) {

        List<PortfolioDTOUsernameLevel> portfolios = portfolioService.getUsersPortfolios(username);
        return ResponseEntity.ok(portfolios);
    }
    
    @GetMapping("/users/{username}/portfolios/{portfolioname}")
    // If you look at your own portfolio, you get a more detailed view than if you check another ones.
    public ResponseEntity<PortfolioDTO> getUserPortfolioByPortfolioname(@PathVariable String username,
                                                                        @PathVariable String portfolioname){

        PortfolioDTO portfolio = portfolioService.getUserPortfolioByPortfolioname(username, portfolioname);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/users/{username}/portfolios")
    @PreAuthorize("@usernameFormatter.format(#username) == authentication.name")
    public ResponseEntity<PortfolioCreationResponse> createPortfolio(@Valid @RequestBody PortfolioCreationRequest creationRequest
            , @PathVariable String username){

        PortfolioCreationResponse creationResponse = portfolioService.createPortfolio(creationRequest, username);
        return ResponseEntity.ok(creationResponse);
    }

    @DeleteMapping("/users/{username}/portfolios/{portfolioname}")
    @PreAuthorize("@usernameFormatter.format(#username) == authentication.name")
    public ResponseEntity<PortfolioDeletionResponse> deletePortfolio(@PathVariable String username,
                                                                     @PathVariable String portfolioname){

        PortfolioDeletionResponse response = portfolioService.deletePortfolio(portfolioname);
        return ResponseEntity.ok(response);
    }
}
