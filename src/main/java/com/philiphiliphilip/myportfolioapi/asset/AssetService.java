package com.philiphiliphilip.myportfolioapi.asset;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.exception.AssetNotFoundException;
import com.philiphiliphilip.myportfolioapi.exception.AuthorizationException;
import com.philiphiliphilip.myportfolioapi.exception.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import com.philiphiliphilip.myportfolioapi.exception.PortfolioNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.transaction.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private AssetRepository assetRepository;
    private TransactionRepository transactionRepository;

    public AssetService(UserRepository userRepository, PortfolioRepository portfolioRepository,
                        AssetRepository assetRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
    }

    private AssetDTO assetConverter(Asset asset){
        AssetDTO assetDTO = new AssetDTO();

        assetDTO.setId(asset.getId());
        assetDTO.setTickerSymbol(asset.getTickerSymbol());
        assetDTO.setQuantity(asset.getQuantity());
        assetDTO.setPurchasePrice(asset.getPurchasePrice());
        assetDTO.setCurrentPrice(asset.getCurrentPrice());
        assetDTO.setTotalInvested(asset.getTotalInvested());
        assetDTO.setValueNow(asset.getValueNow());
        assetDTO.setProfitFactor(asset.getProfitFactor());
        assetDTO.setGrossProfitDollars(asset.getGrossProfitDollars());
        assetDTO.setNetProfitDollars(asset.getNetProfitDollars());
        assetDTO.setTaxRate(asset.getTaxRate());
        assetDTO.setPortfolio(asset.getPortfolio().getName());
        assetDTO.setTransactions(asset.getTransactions());

        return assetDTO;
    }

    public List<AssetDTO> getAllAssets(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        return user.getPortfolio().stream().flatMap(portfolio -> portfolio.getAssets().stream())
                .map(this::assetConverter).collect(Collectors.toList());
    }

    public List<AssetDTO> getPortfolioAssets(Integer userId, Integer portfolioId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        Portfolio userPortfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        if(!user.getPortfolio().contains(userPortfolio)){
            throw new AuthorizationException("User does not own this portfolio.");
        }
        return userPortfolio.getAssets().stream().map(this::assetConverter).collect(Collectors.toList());
    }

    public ResponseEntity<Asset> createAsset(Asset asset, Integer userId, Integer portfolioId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        Portfolio userPortfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        if(!user.getPortfolio().contains(userPortfolio)){
            throw new AuthorizationException("User does not own this portfolio.");
        }

        // To do: Shouldnt be able to create an asset which already exists in portfolio (base it on tickerSymbol)
        asset.setPortfolio(userPortfolio);
        assetRepository.save(asset);

        userPortfolio.getAssets().add(asset);
        portfolioRepository.save(userPortfolio);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    public void deleteAsset(Integer userId, Integer portfolioId, Integer assetId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        Portfolio userPortfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        if(!user.getPortfolio().contains(userPortfolio)){
            throw new AuthorizationException("User does not own this portfolio.");
        }
        Asset userAsset = assetRepository.findById(assetId).orElseThrow(() -> new AssetNotFoundException("id: " + assetId));
        if(!userPortfolio.getAssets().contains(userAsset)){
            throw new AuthorizationException("Portfolio does not own this asset.");
        }

        userPortfolio.getAssets().remove(userAsset);
        assetRepository.delete(userAsset);
    }
}
