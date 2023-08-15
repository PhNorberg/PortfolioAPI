package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTO;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsersLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTOUsersLevel> getAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserDTOUsersLevel(user.getUsername()
                , user.getPortfolio().stream().map(portfolio -> new PortfolioDTOUsersLevel(portfolio.getName()
                , portfolio.getValueNow(), portfolio.getProfitFactor()))
                .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    public User getUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("id:"+id);
        }
        return user.get();
    }

    public ResponseEntity<User> createUser(User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    public void deleteUser(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("id:"+id);
        }
        userRepository.deleteById(id);
    }
}
