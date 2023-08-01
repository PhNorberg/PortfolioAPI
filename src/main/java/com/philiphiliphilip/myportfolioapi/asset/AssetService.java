package com.philiphiliphilip.myportfolioapi.asset;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

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

    public List<AssetDTO> getAllAssets(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        return user.getPortfolio().stream().flatMap(portfolio -> portfolio.getAssets().stream())
                .map(this::assetConverter).collect(Collectors.toList());
    }

    private AssetDTO assetConverter(Asset asset){
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setId(asset.getId());
        assetDTO.setTickerSymbol(asset.getTickerSymbol());
        assetDTO.setQuantity(asset.getQuantity());
        assetDTO.setPurchasePrice(asset.getPurchasePrice()); // purchasePrice depends on the transactions, fix later
        assetDTO.setTaxRate(asset.getTaxRate());
        assetDTO.setPortfolio(asset.getPortfolio().getName());
        assetDTO.setTransactions(asset.getTransactions());

        return assetDTO;
    }
}
