package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    public Portfolio getPortfolioById(Integer userId, Integer portfolioId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));

        return user.getPortfolio()
                .stream()
                .filter(portfolio -> portfolio.getId().equals(portfolioId)).findFirst()
                .orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));

    }

    public ResponseEntity<Portfolio> createPortfolio(Portfolio portfolio, Integer id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id:" + id));
        portfolio.setUser(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        user.getPortfolio().add(portfolio);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPortfolio.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public void deletePortfolio(Integer portfolioId) {
        portfolioRepository.deleteById(portfolioId);

    }
}
