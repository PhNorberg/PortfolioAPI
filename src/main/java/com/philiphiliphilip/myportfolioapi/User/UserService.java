package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsersLevel;
import org.apache.commons.lang3.StringUtils;
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
        username = username.toLowerCase();
        String capitalizedUsername = StringUtils.capitalize(username);
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        if (user.isEmpty()){
            throw new UserNotFoundException(capitalizedUsername);
        }

        return new UserDTOUsernameLevel(user.get().getUsername(), user.get().getPortfolio().stream().map(portfolio -> new
                PortfolioDTOUsernameLevel(portfolio.getName(), portfolio.getValueNow())).collect(Collectors.toList()));
    }

    public void deleteUser(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("id:"+id);
        }
        userRepository.deleteById(id);
    }
}
