package com.philiphiliphilip.myportfolioapi.User.repository;

import com.philiphiliphilip.myportfolioapi.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    int deleteByUsername(String username);
}
