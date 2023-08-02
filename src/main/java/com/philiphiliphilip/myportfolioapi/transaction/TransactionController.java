package com.philiphiliphilip.myportfolioapi.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/users/{userId}/portfolios/transactions")
    public List<TransactionDTO> getAllTransactions(@PathVariable Integer userId){
        return transactionService.getAllTransactions(userId);
    }

    @GetMapping("/users/{userId}/portfolios/{portfolioId}/transactions")
    public List<TransactionDTO> getPortfolioTransactions(@PathVariable Integer userId,
                                                      @PathVariable Integer portfolioId) throws AccessDeniedException {
        return transactionService.getPortfolioTransactions(userId, portfolioId);
    }

    @GetMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}/transactions")
    public List<TransactionDTO> getAssetTransactions(@PathVariable Integer userId,
                                                     @PathVariable Integer portfolioId,
                                                     @PathVariable Integer assetId) throws AccessDeniedException {
        return transactionService.getAssetTransactions(userId, portfolioId, assetId);
    }

    @PostMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}/transactions")
    public ResponseEntity<Transaction> createAssetTransaction(@PathVariable Integer userId,
                                                               @PathVariable Integer portfolioId,
                                                               @PathVariable Integer assetId,
                                                              @RequestBody Transaction transaction) throws AccessDeniedException {
        return transactionService.createAssetTransaction(userId, portfolioId, assetId, transaction);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}/transactions/{transactionId}")
    public void deleteAssetTransaction(@PathVariable Integer userId,
                                                  @PathVariable Integer portfolioId,
                                                  @PathVariable Integer assetId,
                                       @PathVariable Integer transactionId) throws AccessDeniedException {
        transactionService.deleteAssetTransaction(userId, portfolioId, assetId, transactionId);
    }
}
