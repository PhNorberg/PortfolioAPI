package com.philiphiliphilip.myportfolioapi.portfolio.repository;

import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

}
