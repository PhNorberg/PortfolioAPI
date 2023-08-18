package com.philiphiliphilip.myportfolioapi.User.service;

import com.philiphiliphilip.myportfolioapi.User.dto.UserDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.User.dto.UserDTOUsersLevel;
import com.philiphiliphilip.myportfolioapi.User.model.User;
import com.philiphiliphilip.myportfolioapi.User.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.User.response.UserDeleteResponse;
import com.philiphiliphilip.myportfolioapi.exception.UserDeletionFailedException;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsersLevel;
import com.philiphiliphilip.myportfolioapi.utility.NameFormatter;
import jakarta.transaction.Transactional;
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
    private NameFormatter usernameFormatter;

    public UserService(UserRepository userRepository, @Qualifier("usernameFormatter") NameFormatter usernameFormatter) {
        this.userRepository = userRepository;
        this.usernameFormatter = usernameFormatter;
    }

    public List<UserDTOUsersLevel> getAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserDTOUsersLevel(user.getUsername()
                , user.getPortfolio().stream().map(portfolio -> new PortfolioDTOUsersLevel(portfolio.getName()))
                .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    public UserDTOUsernameLevel retrieveUserByUsername(String username){

        // Lower-case and capitalize the first letter of inputted username
        String capitalizedUsername = usernameFormatter.format(username);
        // Check if user exists
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        return new UserDTOUsernameLevel(user.get().getUsername(), user.get().getPortfolio().stream()
                .map(portfolio -> new
                PortfolioDTOUsernameLevel(portfolio.getName(), portfolio.getValueNow())).collect(Collectors.toList()));
    }
    @Transactional
    public UserDeleteResponse deleteUser(String callerUsername){

        // Check if user exists. Might be over the top, only in edge cases might there
        // be a deletion of this user in between the time we go from controller->service layer.
        Optional<User> user = userRepository.findByUsername(callerUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(callerUsername);
        }
        // Attempt to delete, rowsAffected will be 0 if fail.
        int rowsAffected = userRepository.deleteByUsername(callerUsername);
        // Verify deletion

        if (rowsAffected > 0){
            // Successful deletion
            log.debug("User with username {} successfully deleted.", callerUsername);
            return new UserDeleteResponse();
        } else {
            // Failed deletion
            throw new UserDeletionFailedException(callerUsername);
        }
    }
}
