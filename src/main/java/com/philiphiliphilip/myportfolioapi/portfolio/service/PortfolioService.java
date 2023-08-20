package com.philiphiliphilip.myportfolioapi.portfolio.service;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.exception.*;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.converter.PortfolioConverter;
import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTO;
import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.repository.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.request.PortfolioCreationRequest;
import com.philiphiliphilip.myportfolioapi.portfolio.response.PortfolioCreationResponse;
import com.philiphiliphilip.myportfolioapi.portfolio.response.PortfolioDeletionResponse;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final static Logger log = LoggerFactory.getLogger(PortfolioService.class);
    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;
    private PortfolioConverter portfolioConverter;
    private NameFormatter usernameFormatter;
    private NameFormatter portfolionameFormatter;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository,
                            PortfolioConverter portfolioConverter, @Qualifier("usernameFormatter") NameFormatter usernameFormatter,
                            @Qualifier("portfolionameFormatter") NameFormatter portfolionameFormatter) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.portfolioConverter = portfolioConverter;
        this.usernameFormatter = usernameFormatter;
        this.portfolionameFormatter = portfolionameFormatter;
    }

    public List<PortfolioDTOUsernameLevel> getUsersPortfolios(String username){

        // Format username input. Get calling user and log the GET attempt
        String capitalizedUsername = usernameFormatter.format(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("{} attempting to get all portfolios of user {}.", callerUsername, capitalizedUsername);

        // Check that user with the username exists
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        // Fetch list of users portfolios with updated real-time statistics
        List<Portfolio> portfolios = user.get().getPortfolio().stream().
                peek(Portfolio::updatePartialStatistics).toList();

        // Turn into DTO's
        List<PortfolioDTOUsernameLevel> portfolioDtos = portfolios.stream()
                .map(portfolio -> portfolioConverter.convertToUsernameLevel(portfolio)).toList();

        // Save updated portfolios to database
        portfolioRepository.saveAll(portfolios);

        // Log successful attempt.
        log.debug("{} attempt to get all portfolios of user {} succeeded.", callerUsername, capitalizedUsername);

        // Return DTO's to user
        return portfolioDtos;
    }

    /**
     * This method returns different DTO Depending on if a user calls his own portfolio or someone else's
     * Either PortfolioDTOPortfolionameLevelSelf if its his own, else PortfolioDTOPortfolionameLevelOther.
     **/
    public PortfolioDTO getUserPortfolioByPortfolioname(String username, String portfolioname) {

        // Format username and portfolioname input. Get calling user and log the GET attempt
        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("{} attempting to get portfolio {} of user {}.", callerUsername, capitalizedPortfolioname, capitalizedUsername);

        // Check that the username exist in db.
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        // Check if the user is calling his own portfolio or some others,
        boolean isSame = callerUsername.equals(capitalizedUsername);

        // Check if user owns portfolio
        Optional<Portfolio> portfolioOpt = user.get().getPortfolio().stream()
                .filter(p -> p.getName().equals(capitalizedPortfolioname)).findFirst();

        // If not, throw exception
        if (portfolioOpt.isEmpty()){
            throw new PortfolioNotFoundException(capitalizedPortfolioname);
        }

        // Update portfolio with real-time statistics and save to database
        Portfolio portfolio = portfolioOpt.get();
        portfolio.updatePartialStatistics();
        portfolioRepository.save(portfolio);

        // Convert into correct DTO type based on if user is calling his own portfolio or someone elses
        PortfolioDTO portfolioDTO = isSame ?
                portfolioConverter.convertToPortfolionameLevelSelf(portfolio) :
                portfolioConverter.convertToPortfolionameLevelOther(portfolio);

        // Log success
        log.debug("{} attempt to get portfolio {} of user {} succeeded", callerUsername,capitalizedPortfolioname,capitalizedUsername);

        return portfolioDTO;
    }

    @Transactional
    public PortfolioCreationResponse createPortfolio(PortfolioCreationRequest creationRequest, String username){

        // Lower-case and capitalize first letter of username
        String capitalizedUsername = usernameFormatter.format(username);

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
        String modifiedPortfolioName = portfolionameFormatter.format(creationRequest.getName());
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

    @Transactional
    public PortfolioDeletionResponse deletePortfolio(String portfolioname) {

        // Fetch user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String capitalizedUsername = authentication.getName();
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);


        // Format portfolioname and delete it from the user if he is the owner of the portfolio, else throw exception
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        log.debug("{} attempting to delete portfolio {}", capitalizedUsername, capitalizedPortfolioname);

        Portfolio portfolio = user.get().getPortfolio().stream().filter(p ->
                        p.getName().equals(capitalizedPortfolioname)).findFirst()
                .orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        // Remove portfolio from users list of portfolios
        user.get().getPortfolio().remove(portfolio);

        // Delete the portfolio from the database
        portfolioRepository.delete(portfolio);

        // Save user
        userRepository.save(user.get());

        // Verify that portfolio was deleted
        Optional<Portfolio> deletedPortfolio = portfolioRepository.findById(portfolio.getId());
        if (deletedPortfolio.isPresent()){
            throw new PortfolioDeletionFailedException(capitalizedPortfolioname);
        }

        // Log if successful and return response
        log.debug("{} attempt to delete portfolio {} was successful.", capitalizedUsername, capitalizedPortfolioname);
        return new PortfolioDeletionResponse(capitalizedPortfolioname);
    }
}
