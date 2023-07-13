package com.philiphiliphilip.myportfolioapi.User;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.asset.Asset;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import jakarta.persistence.*;

import java.util.List;

// Added to prevent infinite recursion when accessing /users. Without it Jackson will serialize User which has a
// Portfolio as member variable, which it will serialize. This Portfolio points back to the User, which it then will
// serialize again. And that User has a Portfolio in it... etc.
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username"
)
// user is key-name in h2, therefore name it something else
@Entity(name = "user_details")
    public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String email;

    // CascadeType.REMOVE to cascade delete a User's portfolio when a User is deleted
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Portfolio> portfolio;

    public User() {
    }

    public User(Integer id, String username, String email, List<Portfolio> portfolio) {
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
