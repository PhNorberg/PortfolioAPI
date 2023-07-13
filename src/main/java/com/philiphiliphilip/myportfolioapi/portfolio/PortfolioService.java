package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class PortfolioService {

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    public List<Portfolio> getAllPortfolios(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("id:" + id);
        }
        return user.get().getPortfolio();
    }

    public Portfolio getPortfolioById(Integer userId, Integer portfolioID) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("id:" + userId);
        }

        // Should make PortfolioNotFoundException here aswell.
        // Find correct portfolio in User's list of portfolios.


    }
}
