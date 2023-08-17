package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNameAlreadyExistsException;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNameNotAcceptedException;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNotFoundException;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final static Logger log = LoggerFactory.getLogger(PortfolioService.class);
    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;
    private PortfolioConverter portfolioConverter;
    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository,
                            PortfolioConverter portfolioConverter) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.portfolioConverter = portfolioConverter;
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

    /**
     * This method returns different DTO Depending on if a user calls his own portfolio or someone else's
     * Either PortfolioDTOPortfolionameLevelSelf if its his own, else PortfolioDTOPortfolionameLevelOther.
     **/
    public PortfolioDTO getUserPortfolioByPortfolioname(String username, String portfolioname) {

        // Format username and portfolioname input. Get calling user and log the GET attempt
        String capitalizedUsername = StringUtils.capitalize(username.toLowerCase());
        String capitalizedPortfolioname = StringUtils.capitalize(portfolioname.replace(" ", "-").toLowerCase());
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

        // Return proper portfolio or an exception if portfolio not found.
        PortfolioDTO portfolio = user.get().getPortfolio().stream().filter(p -> p.getName().equals(capitalizedPortfolioname))
                .findFirst().map(p -> isSame ? portfolioConverter.convertToPortfolionameLevelSelf(p) :
                        portfolioConverter.convertToPortfolionameLevelOther(p))
                .orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        log.debug("{} attempt to get portfolio {} of user {} succeeded", callerUsername,capitalizedPortfolioname,capitalizedUsername);

        return portfolio;
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
