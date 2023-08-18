package com.philiphiliphilip.myportfolioapi.asset;

import com.philiphiliphilip.myportfolioapi.User.model.User;
import com.philiphiliphilip.myportfolioapi.exception.*;
import com.philiphiliphilip.myportfolioapi.User.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.repository.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.transaction.TransactionRepository;
import com.philiphiliphilip.myportfolioapi.utility.NameFormatter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final static Logger log = LoggerFactory.getLogger(AssetService.class);
    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private AssetRepository assetRepository;
    private TransactionRepository transactionRepository;
    private NameFormatter usernameFormatter;
    private NameFormatter portfolionameFormatter;

    public AssetService(UserRepository userRepository, PortfolioRepository portfolioRepository,
                        AssetRepository assetRepository, TransactionRepository transactionRepository,
                        @Qualifier("usernameFormatter") NameFormatter usernameFormatter,
                        @Qualifier("portfolionameFormatter") NameFormatter portfolionameFormatter) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
        this.usernameFormatter = usernameFormatter;
        this.portfolionameFormatter = portfolionameFormatter;
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

    @Transactional
    public AssetCreationResponse createAsset(AssetCreationRequest assetCreationRequest, String username,
                                             String portfolioname) {

        // Log attempt of creating asset X to portfolio Y.
        // Format username and portfolioname input.
        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        log.debug("{} attempting to create asset {} in portfolio {}.", capitalizedUsername,
                assetCreationRequest.getTickerSymbol(), capitalizedPortfolioname);


        // Check if portfolio actually belongs to user. Throw exception if not.
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        Portfolio portfolio = user.get().getPortfolio().stream().filter(p -> p.getName().equals(capitalizedPortfolioname))
                .findFirst().orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        // Make sure that asset with tickersymbol isnt already in the portfolio. If so, throw exception saying
        // that it does. Also tell the user if he wishes to add to his position, go to /transactions
        String tickerSymbol = assetCreationRequest.getTickerSymbol().toUpperCase();
        boolean assetExists = portfolio.getAssets().stream().anyMatch(asset -> asset.getTickerSymbol().equals(tickerSymbol));
        if (assetExists){
            throw new AssetAlreadyExistsException(tickerSymbol);
        }

        // Create the asset and add it to the users portfolio and then save the portfolio (cascading takes care of the saves)
        Asset asset = new Asset(tickerSymbol, assetCreationRequest.getAssetType(),
                assetCreationRequest.getTaxRate());
        asset.setPortfolio(portfolio);
        //assetRepository.save(asset);
        // Add the asset to the users portfolio and save portfoliorepo
        portfolio.getAssets().add(asset);
        portfolioRepository.save(portfolio);

        // return assetcreationresponse
        log.debug("{} attempt to create asset {} in portfolio {} succeeded.", capitalizedUsername,
                assetCreationRequest.getTickerSymbol(), capitalizedPortfolioname);
        return new AssetCreationResponse(tickerSymbol);
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
