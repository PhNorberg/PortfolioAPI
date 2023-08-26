package com.philiphiliphilip.myportfolioapi.asset.service;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.asset.dto.AssetDTO;
import com.philiphiliphilip.myportfolioapi.asset.model.Asset;
import com.philiphiliphilip.myportfolioapi.asset.repository.AssetRepository;
import com.philiphiliphilip.myportfolioapi.asset.request.AssetCreationRequest;
import com.philiphiliphilip.myportfolioapi.asset.response.AssetCreationResponse;
import com.philiphiliphilip.myportfolioapi.asset.response.AssetDeletionResponse;
import com.philiphiliphilip.myportfolioapi.exception.*;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.repository.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.transaction.repository.TransactionRepository;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssetService {

    private final static Logger log = LoggerFactory.getLogger(AssetService.class);
    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private AssetRepository assetRepository;
    private TransactionRepository transactionRepository;
    private NameFormatter usernameFormatter;
    private NameFormatter portfolionameFormatter;
    private NameFormatter tickersymbolFormatter;

    public AssetService(UserRepository userRepository, PortfolioRepository portfolioRepository,
                        AssetRepository assetRepository, TransactionRepository transactionRepository,
                        @Qualifier("usernameFormatter") NameFormatter usernameFormatter,
                        @Qualifier("portfolionameFormatter") NameFormatter portfolionameFormatter,
                        @Qualifier("tickersymbolFormatter") NameFormatter tickersymbolFormatter) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
        this.usernameFormatter = usernameFormatter;
        this.portfolionameFormatter = portfolionameFormatter;
        this.tickersymbolFormatter = tickersymbolFormatter;
    }

//    private AssetDTO assetConverter(Asset asset){
//        AssetDTO assetDTO = new AssetDTO();
//
//        assetDTO.setId(asset.getId());
//        assetDTO.setTickerSymbol(asset.getTickerSymbol());
//        assetDTO.setQuantity(asset.getQuantity());
//        assetDTO.setPurchasePrice(asset.getPurchasePrice());
//        assetDTO.setCurrentPrice(asset.getCurrentPrice());
//        assetDTO.setTotalInvested(asset.getTotalInvested());
//        assetDTO.setValueNow(asset.getValueNow());
//        assetDTO.setProfitFactor(asset.getProfitFactor());
//        assetDTO.setGrossProfitDollars(asset.getGrossProfitDollars());
//        assetDTO.setNetProfitDollars(asset.getNetProfitDollars());
//        assetDTO.setTaxRate(asset.getTaxRate());
//        assetDTO.setPortfolio(asset.getPortfolio().getName());
//        assetDTO.setTransactions(asset.getTransactions());
//
//        return assetDTO;
//    }
    /*
    To be implemented.
     */
//    public List<AssetDTO> getAllAssets(Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        return user.getPortfolio().stream().flatMap(portfolio -> portfolio.getAssets().stream())
//                .map(this::assetConverter).collect(Collectors.toList());
//    }
    /*
     To be implemented.
     */
//    public List<AssetDTO> getPortfolioAssets(Integer userId, Integer portfolioId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        Portfolio userPortfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
//        if(!user.getPortfolio().contains(userPortfolio)){
//            throw new AuthorizationException("User does not own this portfolio.");
//        }
//        return userPortfolio.getAssets().stream().map(this::assetConverter).collect(Collectors.toList());
//    }

    @Transactional
    public AssetCreationResponse createAsset(AssetCreationRequest assetCreationRequest, String username,
                                             String portfolioname) {

        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        String uppercaseTickersymbol = tickersymbolFormatter.format(assetCreationRequest.getTickerSymbol());
        log.debug("{} attempting to create asset {} in portfolio {}.", capitalizedUsername,
                uppercaseTickersymbol, capitalizedPortfolioname);


        Optional<User> user = userRepository.findByUsername(capitalizedUsername);
        Portfolio portfolio = user.get().getPortfolio().stream().filter(p -> p.getName().equals(capitalizedPortfolioname))
                .findFirst().orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        boolean assetExists = portfolio.getAssets().stream()
                .anyMatch(asset -> asset.getTickerSymbol().equals(uppercaseTickersymbol));
        if (assetExists){
            throw new AssetAlreadyExistsException(uppercaseTickersymbol);
        }

        Asset asset = new Asset(uppercaseTickersymbol, assetCreationRequest.getAssetType(),
                assetCreationRequest.getTaxRate());
        asset.setPortfolio(portfolio);

        portfolio.getAssets().add(asset);
        portfolioRepository.save(portfolio);

        log.debug("{} attempt to create asset {} in portfolio {} succeeded.", capitalizedUsername,
                uppercaseTickersymbol, capitalizedPortfolioname);
        return new AssetCreationResponse(uppercaseTickersymbol);
    }

    public AssetDeletionResponse deleteAsset(String username, String portfolioname, String tickersymbol) {

        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        String uppercaseTickersymbol = tickersymbolFormatter.format(tickersymbol);
        log.debug("{} attempting to delete asset {} in portfolio {}.", capitalizedUsername,
                uppercaseTickersymbol, capitalizedPortfolioname);

        Optional<User> user = userRepository.findByUsername(capitalizedUsername);

        Portfolio portfolio = user.get().getPortfolio().stream()
                .filter(p -> p.getName().equals(capitalizedPortfolioname))
                .findFirst()
                .orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        Asset asset = portfolio.getAssets().stream()
                .filter(a -> a.getTickerSymbol().equals(uppercaseTickersymbol))
                .findFirst()
                .orElseThrow(() -> new AssetNotFoundException(uppercaseTickersymbol));

        portfolio.getAssets().remove(asset);
        assetRepository.delete(asset);

        portfolio.updateStatistics();

        portfolioRepository.save(portfolio);
        log.debug("{} attempt to delete asset {} in portfolio {} succeeded.", capitalizedUsername,
                uppercaseTickersymbol, capitalizedPortfolioname);

        return new AssetDeletionResponse(uppercaseTickersymbol);
    }
}
