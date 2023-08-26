package com.philiphiliphilip.myportfolioapi.user.repository;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
