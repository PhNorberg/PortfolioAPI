package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNameAlreadyExistsException;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNameNotAcceptedException;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final static Logger log = LoggerFactory.getLogger(PortfolioService.class);
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
    public List<PortfolioDTOUsernameLevel> getAllPortfolios(String username){

        // Format username input. Get calling user and log the GET attempt
        String capitalizedUsername = StringUtils.capitalize(username.toLowerCase());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("{} attempting to get all portfolios of user {}.", callerUsername, capitalizedUsername);

        // Check that username is legit
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        // Fetch list of users portfolios
        List<PortfolioDTOUsernameLevel> portfolios = user.get().getPortfolio().stream().map(portfolio ->
                new PortfolioDTOUsernameLevel(portfolio.getName(), portfolio.getValueNow())).toList();

        // Log successful attempt.
        log.debug("{} attempt to get all portfolios of user {} succeeded.", callerUsername, capitalizedUsername);

        return portfolios;
    }

    public PortfolioDTOPortfoliosLevel getUserPortfolioByPortfolioname(String username, String portfolioname) {
        // Check if user exists
        //User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        // Check if portfolio exists
//        return user.getPortfolio()
//                .stream()
//                .filter(portfolio -> portfolio.getId().equals(portfolioId)).findFirst().map(this::portfolioConverter).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        return null;
    }
    @Transactional
    public PortfolioCreationResponse createPortfolio(PortfolioCreationRequest creationRequest, String username){

        // Lower-case and capitalize first letter of username
        String capitalizedUsername = StringUtils.capitalize(username.toLowerCase());

        // Log user attempt to create new portfolio
        log.debug("User with username " + capitalizedUsername + " attempting to create new portfolio.");

        // Check validity of portfolio name. Cannot be special characters, spaces at beginning nor end,
        // or more than one space between words.
        String pattern = "^[A-Za-z0-9_-]+( [A-Za-z0-9_-]+)*$";
        if (!creationRequest.getName().matches(pattern)){
            throw new PortfolioNameNotAcceptedException(creationRequest.getName());
        }

        // Fetch user
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);

        // Add hyphens to portfolio name containing several words, lower-case the letters and capitalize first letter
        String modifiedPortfolioName = StringUtils.capitalize(creationRequest.getName().replace(" ", "-").toLowerCase());

        // Check if user already owns portfolio with this name.
        if (user.get().getPortfolio().stream().anyMatch(portfolio -> portfolio.getName().equals(modifiedPortfolioName))){
            throw new PortfolioNameAlreadyExistsException(modifiedPortfolioName);
        }

        // All is well, create portfolio and save it to the database
        Portfolio newPortfolio = new Portfolio(modifiedPortfolioName, user.get());
        portfolioRepository.save(newPortfolio);

        // Set user as owner of the portfolio and save it to the database aswell
        user.get().getPortfolio().add(newPortfolio);
        userRepository.save(user.get());

        // Log the successful attempt
        log.debug("User with username " + capitalizedUsername + " succesfully created portfolio with name " + modifiedPortfolioName
        + ".");
        return new PortfolioCreationResponse(modifiedPortfolioName);
    }

    public void deletePortfolio(Integer portfolioId) {
        portfolioRepository.deleteById(portfolioId);

    }
}
