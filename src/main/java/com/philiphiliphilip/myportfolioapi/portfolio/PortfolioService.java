package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    private PortfolioDTO portfolioConverter(Portfolio portfolio){
        PortfolioDTO portfolioDTO = new PortfolioDTO();

        portfolioDTO.setId(portfolio.getId());
        portfolioDTO.setName(portfolio.getName());
        portfolioDTO.setTotalInvested(portfolio.getTotalInvested());
        portfolioDTO.setValueNow(portfolio.getValueNow());
        portfolioDTO.setProfitFactor(portfolio.getProfitFactor());
        portfolioDTO.setGrossProfitDollars(portfolio.getGrossProfitDollars());
        portfolioDTO.setNetProfitDollars(portfolio.getNetProfitDollars());
        portfolioDTO.setAssets(portfolio.getAssets());

        return portfolioDTO;
    }
    public List<PortfolioDTO> getAllPortfolios(Integer id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id:" + id));

        return user.getPortfolio().stream().map(this::portfolioConverter).collect(Collectors.toList());
    }

    public PortfolioDTO getPortfolioById(Integer userId, Integer portfolioId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));

        return user.getPortfolio()
                .stream()
                .filter(portfolio -> portfolio.getId().equals(portfolioId)).findFirst().map(this::portfolioConverter).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));

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
