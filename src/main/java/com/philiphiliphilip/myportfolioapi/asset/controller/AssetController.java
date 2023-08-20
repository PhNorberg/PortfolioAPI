package com.philiphiliphilip.myportfolioapi.asset.controller;

import com.philiphiliphilip.myportfolioapi.asset.request.AssetCreationRequest;
import com.philiphiliphilip.myportfolioapi.asset.response.AssetCreationResponse;
import com.philiphiliphilip.myportfolioapi.asset.response.AssetDeletionResponse;
import com.philiphiliphilip.myportfolioapi.asset.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AssetController {

    private AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /*
     To be implemented.
     */
    //    @GetMapping("/users/{userId}/portfolios/assets")
//    public List<AssetDTO> getAllAssets(@PathVariable Integer userId){
//        return assetService.getAllAssets(userId);
//    }
//
    /*
     To be implemented.
     */
//    @GetMapping("/users/{userId}/portfolios/{portfolioId}/assets")
//    public List<AssetDTO> getPortfolioAssets(@PathVariable Integer userId, @PathVariable Integer portfolioId){
//        return assetService.getPortfolioAssets(userId, portfolioId);
//    }

    @PostMapping("/users/{username}/portfolios/{portfolioname}/assets")
    @PreAuthorize("@usernameFormatter.format(#username) == authentication.name")
    public ResponseEntity<AssetCreationResponse> createAsset(@Valid @RequestBody AssetCreationRequest assetCreationRequest,
                                                             @PathVariable String username,
                                                             @PathVariable String portfolioname){

        AssetCreationResponse response = assetService.createAsset(assetCreationRequest, username, portfolioname);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{username}/portfolios/{portfolioname}/{tickersymbol}")
    @PreAuthorize("@usernameFormatter.format(#username) == authentication.name")
    public ResponseEntity<AssetDeletionResponse> deleteAsset(@PathVariable String username, @PathVariable String portfolioname,
                                                             @PathVariable String tickersymbol){

        AssetDeletionResponse response = assetService.deleteAsset(username, portfolioname, tickersymbol);
        return ResponseEntity.ok().body(response);

    }
}
