package com.philiphiliphilip.myportfolioapi.User;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.Asset;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import jakarta.persistence.OneToMany;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username"
)
public class User {

    private Integer id;
    private String username;
    private String email;

    //@OneToMany(mappedBy = "user")
    private List<Portfolio> portfolio;

    public User(Integer id, String username,  String email, List<Portfolio> portfolio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.portfolio = portfolio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Portfolio> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<Portfolio> portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", portfolio=" + portfolio +
                '}';
    }
}
