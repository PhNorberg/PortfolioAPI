package com.philiphiliphilip.myportfolioapi.user.service;

import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.repository.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.user.converter.UserConverter;
import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsersLevel;
import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.user.response.UserDeleteResponse;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private NameFormatter usernameFormatter;
    private UserConverter userConverter;

    public UserService(UserRepository userRepository,
                       PortfolioRepository portfolioRepository,
                       @Qualifier("usernameFormatter") NameFormatter usernameFormatter,
                       UserConverter userConverter) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.usernameFormatter = usernameFormatter;
        this.userConverter = userConverter;
    }

    public List<UserDTOUsersLevel> getAllUsers(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("GET /users was called by user {}.", callerUsername);

        List<UserDTOUsersLevel> response = userRepository.findAll().stream().map(user -> userConverter.
                convertToUsersLevel(user)).toList();

        log.debug("GET /users call was successful by user {}.", callerUsername);
        return response;
    }

    public UserDTOUsernameLevel getUserByUsername(String username){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("GET /users/{} was called by user {}.", username, callerUsername);


        String capitalizedUsername = usernameFormatter.format(username);
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        List<Portfolio> portfolios = user.get().getPortfolio();
        portfolios.forEach(Portfolio::updatePartialStatistics);

        portfolioRepository.saveAll(portfolios);

        UserDTOUsernameLevel response = userConverter.convertToUsernameLevel(user.get());

        log.debug("GET /users/{} call was successful by user {}.", username, callerUsername);
        return response;
    }

    @Transactional
    public UserDeleteResponse deleteUser(String username){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = authentication.getName();
        log.debug("DELETE /users/{} was called by user {}.", username, callerUsername);

        userRepository.deleteByUsername(callerUsername);

        log.debug("User with username {} successfully deleted.", callerUsername);
        return new UserDeleteResponse();

    }
}
