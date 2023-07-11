package com.philiphiliphilip.myportfolioapi.portfolio;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.User.User;
import jakarta.persistence.ManyToOne;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Portfolio {

    private Integer id;
    private List<Asset> assets;
    //@ManyToOne
    private User user;

    public Portfolio(Integer id, List<Asset> assets, User user) {
        this.id = id;
        this.assets = assets;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
                ", assets=" + assets +
                ", user=" + user +
                '}';
    }
}
