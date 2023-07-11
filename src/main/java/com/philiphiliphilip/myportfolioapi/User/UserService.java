package com.philiphiliphilip.myportfolioapi.User;



import com.philiphiliphilip.myportfolioapi.portfolio.Asset;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserService {

    private static List<User> users = new ArrayList<>();
    private static int usersCount = 0;
    private static int assetCount = 0;
    static {
        Asset asset1 = new Asset(++assetCount, "BTC", 10, new BigDecimal(20000), LocalDateTime.now().minusYears(1), 0);
        Asset asset2 = new Asset(++assetCount, "ETH", 100, new BigDecimal(1000), LocalDateTime.now().minusYears(1), 0);
        List<Asset> cryptoAssets = new ArrayList<>();
        cryptoAssets.add(asset1);
        cryptoAssets.add(asset2);
        User cryptoUser = new User(++usersCount, "Crypto Guy", "crypto@gmail.com", new ArrayList<>());
        Portfolio cryptoPortfolio = new Portfolio(100, cryptoAssets, cryptoUser);
        cryptoUser.getPortfolio().add(cryptoPortfolio);
        users.add(cryptoUser);

        Asset asset3 = new Asset(++assetCount, "AAPL", 50, new BigDecimal(100), LocalDateTime.now().minusYears(2), 30);
        Asset asset4 = new Asset(++assetCount, "NVDA", 20, new BigDecimal(150), LocalDateTime.now().minusYears(3), 30);
        List<Asset> stockAssets = new ArrayList<>();
        stockAssets.add(asset3);
        stockAssets.add(asset4);
        User stockUser = new User(++usersCount, "Stock guy", "stocks@gmail.com", new ArrayList<>());
        Portfolio stockPortfolio = new Portfolio(200, stockAssets, stockUser);
        stockUser.getPortfolio().add(stockPortfolio);
        users.add(stockUser);

    }

    public List<User> findAll(){
        return users;
    }

    public User findById(int id) {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
    }

    public User save(User user){
        user.setId(++usersCount);
        users.add(user);
        return user;
    }

    public void deleteById(int id){
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }
}
