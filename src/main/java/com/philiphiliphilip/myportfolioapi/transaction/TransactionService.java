package com.philiphiliphilip.myportfolioapi.transaction;

import com.philiphiliphilip.myportfolioapi.User.User;
import com.philiphiliphilip.myportfolioapi.User.UserNotFoundException;
import com.philiphiliphilip.myportfolioapi.User.UserRepository;
import com.philiphiliphilip.myportfolioapi.asset.Asset;
import com.philiphiliphilip.myportfolioapi.asset.AssetNotFoundException;
import com.philiphiliphilip.myportfolioapi.asset.AssetRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioNotFoundException;
import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private AssetRepository assetRepository;
    private TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, PortfolioRepository portfolioRepository,
                              AssetRepository assetRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
    }

    private TransactionDTO transactionConverter(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setQuantity(transaction.getQuantity());
        transactionDTO.setPurchaseDate(transaction.getPurchaseDate());
        transactionDTO.setPurchasePrice(transaction.getPurchasePrice());
        transactionDTO.setTickerSymbol(transaction.getAsset().getTickerSymbol());
        transactionDTO.setPortfolio(transaction.getAsset().getPortfolio().getName());

        return transactionDTO;
    }

    public List<TransactionDTO> getAllTransactions(Integer userId) {
        // Create a stream of a users portfolio which is a list, that  each contains  list of assets
        // that each contains a list of transactions.
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        return user.getPortfolio().stream().flatMap(portfolio -> portfolio.getAssets().stream())
                .flatMap(asset -> asset.getTransactions().stream()).map(this::transactionConverter).collect(Collectors.toList());
    }

    public List<TransactionDTO> getPortfolioTransactions(Integer userId, Integer portfolioId) throws AccessDeniedException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        // Check if the user owns the portfolio
        if (!user.getPortfolio().contains(portfolio)) {
            throw new AccessDeniedException("User does not own this portfolio");
        }
        return portfolio.getAssets().stream().flatMap(asset -> asset.getTransactions().stream())
                .map(this::transactionConverter).collect(Collectors.toList());
    }

    public List<TransactionDTO> getAssetTransactions(Integer userId, Integer portfolioId, Integer assetId) throws AccessDeniedException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
        // Check if the user owns the portfolio
        if (!user.getPortfolio().contains(portfolio)) {
            throw new AccessDeniedException("User does not own this portfolio");
        }
        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new AssetNotFoundException("id:" + assetId));
        // Check if asset belongs to portfolio
        if (!portfolio.getAssets().contains(asset)){
            throw new AccessDeniedException("Portfolio does not own this asset");
        }
        return asset.getTransactions().stream().map(this::transactionConverter).collect(Collectors.toList());
    }

    public ResponseEntity<Transaction> createAssetTransaction(Integer userId, Integer portfolioId, Integer assetId, Transaction transaction) {
        return null;
    }

    public void deleteAssetTransaction(Integer userId, Integer portfolioId, Integer assetId, Integer transactionId) {

    }
}
