package com.philiphiliphilip.myportfolioapi.portfolio;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PortfolioController {

    private PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @GetMapping("/users/{id}/portfolios")
    public List<Portfolio> getAllPortfolios(@PathVariable Integer id) {
        return portfolioService.getAllPortfolios(id);
    }
    
    @GetMapping("/users/{id}/portfolios/{id}")
    public Portfolio getPortfolioById(@PathVariable Integer userId, @PathVariable Integer portfolioID){
        return portfolioService.getPortfolioById(userId, portfolioID);

    }
}
