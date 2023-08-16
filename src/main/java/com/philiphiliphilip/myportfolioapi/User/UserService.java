package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.exception.UserDeletionFailedException;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsersLevel;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTOUsersLevel> getAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserDTOUsersLevel(user.getUsername()
                , user.getPortfolio().stream().map(portfolio -> new PortfolioDTOUsersLevel(portfolio.getName()))
                .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    public UserDTOUsernameLevel retrieveUserByUsername(String username){

        // Lower-case and capitalize the first letter of inputted username
        String capitalizedUsername = StringUtils.capitalize(username.toLowerCase());
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
