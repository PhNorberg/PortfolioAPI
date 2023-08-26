package com.philiphiliphilip.myportfolioapi.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.asset.model.Asset;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private BigDecimal netProfitDollars;
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    private List<Asset> assets;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Portfolio(String name, User user) {
        this.name = name;
        this.assets = new ArrayList<>();
        this.user = user;
        this.totalInvested = BigDecimal.ZERO;
        this.valueNow = BigDecimal.ZERO;
        this.profitFactor = BigDecimal.ZERO;
        this.grossProfitDollars = BigDecimal.ZERO;
        this.netProfitDollars = BigDecimal.ZERO;
    }

    public Portfolio() {
    }

    public void updateStatistics() {
        updateTotalInvested();
        updateValueNow();
        updateProfitFactor();
        updateGrossProfitDollars();
        updateNetProfitDollars();
    }

    public void updatePartialStatistics(){
        this.getAssets().stream().forEach(Asset::updatePartialStatistics);
        updateValueNow();
        updateProfitFactor();
        updateGrossProfitDollars();
        updateNetProfitDollars();
    }
    private void updateTotalInvested(){
        this.totalInvested = this.assets.stream().map(Asset::getTotalInvested).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.DOWN);
    }

    private void updateValueNow(){
        this.valueNow = this.assets.stream().map(Asset::getValueNow).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.DOWN);
    }

    private void updateProfitFactor(){
        this.profitFactor = (this.valueNow.compareTo(BigDecimal.ZERO) > 0) ?
                (this.valueNow.divide(this.totalInvested, 10, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN) :
                BigDecimal.ZERO;
    }

    private void updateGrossProfitDollars(){
        this.grossProfitDollars = this.assets.stream().map(Asset::getGrossProfitDollars).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.DOWN);
    }

    private void updateNetProfitDollars(){
        this.netProfitDollars = this.assets.stream().map(Asset::getNetProfitDollars).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.DOWN);
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

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public BigDecimal getValueNow() {
        return valueNow;
    }

    public void setValueNow(BigDecimal valueNow) {
        this.valueNow = valueNow;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitFactor) {
        this.profitFactor = profitFactor;
    }

    public BigDecimal getGrossProfitDollars() {
        return grossProfitDollars;
    }

    public void setGrossProfitDollars(BigDecimal grossProfitDollars) {
        this.grossProfitDollars = grossProfitDollars;
    }

    public BigDecimal getNetProfitDollars() {
        return netProfitDollars;
    }

    public void setNetProfitDollars(BigDecimal netProfitDollars) {
        this.netProfitDollars = netProfitDollars;
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
