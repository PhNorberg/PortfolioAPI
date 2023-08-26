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

        String capitalizedUsername = usernameFormatter.format(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("{} attempting to get all portfolios of user {}.", callerUsername, capitalizedUsername);

        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        List<Portfolio> portfolios = user.get().getPortfolio().stream().
                peek(Portfolio::updatePartialStatistics).toList();

        List<PortfolioDTOUsernameLevel> portfolioDtos = portfolios.stream()
                .map(portfolio -> portfolioConverter.convertToUsernameLevel(portfolio)).toList();

        portfolioRepository.saveAll(portfolios);

        log.debug("{} attempt to get all portfolios of user {} succeeded.", callerUsername, capitalizedUsername);

        return portfolioDtos;
    }

    /**
     * This method returns different DTO Depending on if a user calls his own portfolio or someone else's
     * Either PortfolioDTOPortfolionameLevelSelf if its his own, else PortfolioDTOPortfolionameLevelOther.
     **/
    public PortfolioDTO getUserPortfolioByPortfolioname(String username, String portfolioname) {

        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("{} attempting to get portfolio {} of user {}.", callerUsername, capitalizedPortfolioname, capitalizedUsername);

        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        boolean isSame = callerUsername.equals(capitalizedUsername);

        Optional<Portfolio> portfolioOpt = user.get().getPortfolio().stream()
                .filter(p -> p.getName().equals(capitalizedPortfolioname)).findFirst();

        if (portfolioOpt.isEmpty()){
            throw new PortfolioNotFoundException(capitalizedPortfolioname);
        }

        Portfolio portfolio = portfolioOpt.get();
        portfolio.updatePartialStatistics();
        portfolioRepository.save(portfolio);

        PortfolioDTO portfolioDTO = isSame ?
                portfolioConverter.convertToPortfolionameLevelSelf(portfolio) :
                portfolioConverter.convertToPortfolionameLevelOther(portfolio);

        log.debug("{} attempt to get portfolio {} of user {} succeeded", callerUsername,capitalizedPortfolioname,capitalizedUsername);

        return portfolioDTO;
    }

    @Transactional
    public PortfolioCreationResponse createPortfolio(PortfolioCreationRequest creationRequest, String username){

        String capitalizedUsername = usernameFormatter.format(username);

        log.debug("User with username " + capitalizedUsername + " attempting to create new portfolio.");

        String pattern = "^[A-Za-z0-9_-]+( [A-Za-z0-9_-]+)*$";
        if (!creationRequest.getName().matches(pattern)){
            throw new PortfolioNameNotAcceptedException(creationRequest.getName());
        }

        Optional<User> user = userRepository.findByUsername(capitalizedUsername);

        String modifiedPortfolioName = portfolionameFormatter.format(creationRequest.getName());
        if (user.get().getPortfolio().stream().anyMatch(portfolio -> portfolio.getName().equals(modifiedPortfolioName))){
            throw new PortfolioNameAlreadyExistsException(modifiedPortfolioName);
        }

        Portfolio newPortfolio = new Portfolio(modifiedPortfolioName, user.get());
        portfolioRepository.save(newPortfolio);

        user.get().getPortfolio().add(newPortfolio);
        userRepository.save(user.get());

        log.debug("User with username " + capitalizedUsername + " succesfully created portfolio with name " + modifiedPortfolioName
        + ".");
        return new PortfolioCreationResponse(modifiedPortfolioName);
    }

    @Transactional
    public PortfolioDeletionResponse deletePortfolio(String portfolioname) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String capitalizedUsername = authentication.getName();
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);

        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        log.debug("{} attempting to delete portfolio {}", capitalizedUsername, capitalizedPortfolioname);

        Portfolio portfolio = user.get().getPortfolio().stream().filter(p ->
                        p.getName().equals(capitalizedPortfolioname)).findFirst()
                .orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        user.get().getPortfolio().remove(portfolio);
        portfolioRepository.delete(portfolio);
        userRepository.save(user.get());

        log.debug("{} attempt to delete portfolio {} was successful.", capitalizedUsername, capitalizedPortfolioname);
        return new PortfolioDeletionResponse(capitalizedPortfolioname);
    }
}
