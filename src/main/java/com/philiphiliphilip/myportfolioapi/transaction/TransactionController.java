package com.philiphiliphilip.myportfolioapi.transaction;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*
    To be implemented.
     */
//    @GetMapping("/users/{userId}/portfolios/transactions")
//    public List<TransactionDTO> getAllTransactions(@PathVariable Integer userId){
//        return transactionService.getAllTransactions(userId);
//    }

    /*
    To be implemented.
     */
//    @GetMapping("/users/{userId}/portfolios/{portfolioId}/transactions")
//    public List<TransactionDTO> getPortfolioTransactions(@PathVariable Integer userId,
//                                                      @PathVariable Integer portfolioId) throws AccessDeniedException {
//        return transactionService.getPortfolioTransactions(userId, portfolioId);
//    }

    /*
    To be implemented.
     */
//    @GetMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}/transactions")
//    public List<TransactionDTO> getAssetTransactions(@PathVariable Integer userId,
//                                                     @PathVariable Integer portfolioId,
//                                                     @PathVariable Integer assetId) throws AccessDeniedException {
//        return transactionService.getAssetTransactions(userId, portfolioId, assetId);
//    }

    @PostMapping("/users/{username}/portfolios/{portfolioname}/{tickersymbol}/transactions")
    @PreAuthorize("@usernameTransformer.transform(#username) == authentication.name")
    public ResponseEntity<TransactionCreationResponse> createAssetTransaction(@Valid @RequestBody TransactionCreationRequest creationRequest,
                                                              @PathVariable String username,
                                                               @PathVariable String portfolioname,
                                                               @PathVariable String tickersymbol)  {

        TransactionCreationResponse response = transactionService.createAssetTransaction(creationRequest, username
                , portfolioname, tickersymbol);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}/transactions/{transactionId}")
    public void deleteAssetTransaction(@PathVariable Integer userId,
                                                  @PathVariable Integer portfolioId,
                                                  @PathVariable Integer assetId,
                                       @PathVariable Integer transactionId) throws AccessDeniedException {
        transactionService.deleteAssetTransaction(userId, portfolioId, assetId, transactionId);
    }
}
