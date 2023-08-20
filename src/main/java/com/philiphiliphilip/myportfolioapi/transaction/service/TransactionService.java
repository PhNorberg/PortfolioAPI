package com.philiphiliphilip.myportfolioapi.transaction.service;

import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.exception.*;
import com.philiphiliphilip.myportfolioapi.user.repository.UserRepository;
import com.philiphiliphilip.myportfolioapi.asset.model.Asset;
import com.philiphiliphilip.myportfolioapi.asset.repository.AssetRepository;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.portfolio.repository.PortfolioRepository;
import com.philiphiliphilip.myportfolioapi.transaction.dto.TransactionDTO;
import com.philiphiliphilip.myportfolioapi.transaction.model.Transaction;
import com.philiphiliphilip.myportfolioapi.transaction.repository.TransactionRepository;
import com.philiphiliphilip.myportfolioapi.transaction.request.TransactionCreationRequest;
import com.philiphiliphilip.myportfolioapi.transaction.response.TransactionCreationResponse;
import com.philiphiliphilip.myportfolioapi.formatter.NameFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final static Logger log = LoggerFactory.getLogger(TransactionService.class);
    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private AssetRepository assetRepository;
    private TransactionRepository transactionRepository;
    private NameFormatter usernameFormatter;
    private NameFormatter portfolionameFormatter;
    private NameFormatter tickersymbolFormatter;
    private NameFormatter transactiontypeFormatter;

    public TransactionService(UserRepository userRepository, PortfolioRepository portfolioRepository,
                              AssetRepository assetRepository, TransactionRepository transactionRepository,
                              @Qualifier("usernameFormatter") NameFormatter usernameFormatter,
                              @Qualifier("portfolionameFormatter") NameFormatter portfolionameFormatter,
                              @Qualifier("tickersymbolFormatter") NameFormatter tickersymbolFormatter,
                              @Qualifier("transactiontypeFormatter") NameFormatter transactiontypeFormatter) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.transactionRepository = transactionRepository;
        this.usernameFormatter = usernameFormatter;
        this.portfolionameFormatter = portfolionameFormatter;
        this.tickersymbolFormatter = tickersymbolFormatter;
        this.transactiontypeFormatter = transactiontypeFormatter;
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

    /*
    To be implemented.
     */
//    public List<TransactionDTO> getAllTransactions(Integer userId) {
//        // Create a stream of a users portfolio which is a list, that  each contains  list of assets
//        // that each contains a list of transactions.
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        return user.getPortfolio().stream().flatMap(portfolio -> portfolio.getAssets().stream())
//                .flatMap(asset -> asset.getTransactions().stream()).map(this::transactionConverter).collect(Collectors.toList());
//    }
    /*
    To be implemented.
     */
//    public List<TransactionDTO> getPortfolioTransactions(Integer userId, Integer portfolioId) throws AccessDeniedException {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
//        // Check if the user owns the portfolio
//        if (!user.getPortfolio().contains(portfolio)) {
//            throw new AccessDeniedException("User does not own this portfolio");
//        }
//        return portfolio.getAssets().stream().flatMap(asset -> asset.getTransactions().stream())
//                .map(this::transactionConverter).collect(Collectors.toList());
//    }
    /*
    To be implemented.
     */
//    public List<TransactionDTO> getAssetTransactions(Integer userId, Integer portfolioId, Integer assetId) throws AccessDeniedException {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
//        // Check if the user owns the portfolio
//        if (!user.getPortfolio().contains(portfolio)) {
//            throw new AccessDeniedException("User does not own this portfolio");
//        }
//        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new AssetNotFoundException("id:" + assetId));
//        // Check if asset belongs to portfolio
//        if (!portfolio.getAssets().contains(asset)){
//            throw new AccessDeniedException("Portfolio does not own this asset");
//        }
//        return asset.getTransactions().stream().map(this::transactionConverter).collect(Collectors.toList());
//    }

    @Transactional
    public TransactionCreationResponse createAssetTransaction(TransactionCreationRequest creationRequest, String username,
                                                              String portfolioname, String tickersymbol) {

        // Format username, portfolioname, tickersymbol and transactiontype
        String capitalizedUsername = usernameFormatter.format(username);
        String capitalizedPortfolioname = portfolionameFormatter.format(portfolioname);
        String uppercaseTickersymbol = tickersymbolFormatter.format(tickersymbol);
        // Make formatter for this:)
        String lowercaseTransactiontype = creationRequest.getTransactionType().toLowerCase();

        log.debug("User {} trying to add transactiontype {} of asset {} in portfolio {}.",
                capitalizedUsername, lowercaseTransactiontype, uppercaseTickersymbol,
                capitalizedPortfolioname);
        // Fetch user object
        Optional<User> user = userRepository.findByUsername(capitalizedUsername);

        // Fetch portfolio if it belongs to user
        Portfolio portfolio = user.get().getPortfolio().stream()
                .filter(p -> p.getName().equals(capitalizedPortfolioname))
                .findFirst()
                .orElseThrow(() -> new PortfolioNotFoundException(capitalizedPortfolioname));

        // Fetch asset if it belongs to portfolio
        Asset asset = portfolio.getAssets().stream()
                .filter(a -> a.getTickerSymbol().equals(uppercaseTickersymbol))
                .findFirst()
                .orElseThrow(() -> new AssetNotFoundException(uppercaseTickersymbol));

        // If transaction type sell, check asset to see if we have that amount to sell
        if (lowercaseTransactiontype.equals("sell") &&
                asset.getQuantity().compareTo(creationRequest.getQuantity()) < 0){
            throw new AssetQuantityNotEnoughException(asset.getQuantity(), uppercaseTickersymbol);
        }
        // Create the transaction
        Transaction transaction = new Transaction(lowercaseTransactiontype, creationRequest.getQuantity(),
                creationRequest.getPrice());

        // Create relation between objects.
        transaction.setAsset(asset);
        asset.getTransactions().add(transaction);

        // Update asset statistics
        asset.updateStatistics(transaction);

        // Save to database
        //userRepository.save(user.get());

        assetRepository.save(asset);
        log.debug("User {} sucessfully added transactiontype {} of asset {} in portfolio {}.",
                capitalizedUsername, lowercaseTransactiontype, uppercaseTickersymbol,
                capitalizedPortfolioname);
        return new TransactionCreationResponse(lowercaseTransactiontype);
    }
    /*
    To be implemented.
     */
//    @Transactional
//    public void deleteAssetTransaction(Integer userId, Integer portfolioId, Integer assetId, Integer transactionId) throws AccessDeniedException {
//        // Need to update the asset object to have quantity and purchasePrice etc up to date
//        // Make it @Transactional to make sure that the series of db operations are treated as a single atomic unit.
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id:" + userId));
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioNotFoundException("id:" + portfolioId));
//        // Check if the user owns the portfolio
//        if (!user.getPortfolio().contains(portfolio)) {
//            throw new AccessDeniedException("User does not own this portfolio");
//        }
//        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new AssetNotFoundException("id:" + assetId));
//        // Check if asset belongs to portfolio
//        if (!portfolio.getAssets().contains(asset)){
//            throw new AccessDeniedException("Portfolio does not own this asset");
//        }
//        Transaction transaction = transactionRepository.findById(transactionId)
//                .orElseThrow(() -> new TransactionNotFoundException("id:" + transactionId));
//
//        asset.getTransactions().remove(transaction);
//        transactionRepository.delete(transaction);
//        asset.updateStatistics(transaction);
//        assetRepository.save(asset);
//    }
}
