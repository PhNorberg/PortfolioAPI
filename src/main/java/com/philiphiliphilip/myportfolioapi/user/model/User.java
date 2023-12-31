package com.philiphiliphilip.myportfolioapi.user.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import jakarta.persistence.*;

import java.util.List;

// Added to prevent infinite recursion when accessing /users. Without it Jackson will serialize User which has a
// Portfolio as member variable, which it will serialize. This Portfolio points back to the User, which it then will
// serialize again. And that User has a Portfolio in it... etc.
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username"
)

@Entity(name = "user_details")
    public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;


    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    private List<Portfolio> portfolio;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                ", portfolio=" + portfolio +
                '}';
    }
}
