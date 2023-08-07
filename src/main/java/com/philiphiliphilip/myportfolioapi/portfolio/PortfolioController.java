package com.philiphiliphilip.myportfolioapi.portfolio;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PortfolioController {

    private PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @GetMapping("/users/{id}/portfolios")
    public List<PortfolioDTO> getAllPortfolios(@PathVariable Integer id) {
        return portfolioService.getAllPortfolios(id);
    }
    
    @GetMapping("/users/{userId}/portfolios/{portfolioId}")
    public PortfolioDTO getPortfolioById(@PathVariable Integer userId, @PathVariable Integer portfolioId){
        return portfolioService.getPortfolioById(userId, portfolioId);

    }

    @PostMapping("/users/{id}/portfolios")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio, @PathVariable Integer id){
        return portfolioService.createPortfolio(portfolio, id);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}")
    public void deletePortfolio(@PathVariable Integer userId, @PathVariable Integer portfolioId){
        portfolioService.deletePortfolio(portfolioId);
    }
}
