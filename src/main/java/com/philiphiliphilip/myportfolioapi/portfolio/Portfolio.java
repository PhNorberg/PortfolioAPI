package com.philiphiliphilip.myportfolioapi.portfolio;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.asset.Asset;
import jakarta.persistence.*;

import java.util.List;

// Added to prevent infinite recursion when accessing /users. Without it Jackson will serialize User which has a
// Portfolio as member variable, which it will serialize. This Portfolio points back to the User, which it then will
// serialize again. And that User has a Portfolio in it... etc.
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
public class Portfolio {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    // CascadeType.REMOVE to delete all assets in the portfolio when a portfolio is deleted
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.REMOVE)
    private List<Asset> assets;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Portfolio(Integer id, String name, List<Asset> assets, User user) {
        this.id = id;
        this.name = name;
        this.assets = assets;
        this.user = user;
    }

    public Portfolio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", assets=" + assets +
                ", user=" + user +
                '}';
    }
}
