package com.philiphiliphilip.myportfolioapi.asset;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetController {

    private AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/users/{userId}/portfolios/assets")
    public List<AssetDTO> getAllAssets(@PathVariable Integer userId){
        return assetService.getAllAssets(userId);
    }

    @GetMapping("/users/{userId}/portfolios/{portfolioId}/assets")
    public List<AssetDTO> getPortfolioAssets(@PathVariable Integer userId, @PathVariable Integer portfolioId){
        return assetService.getPortfolioAssets(userId, portfolioId);
    }

    @PostMapping("/users/{userId}/portfolios/{portfolioId}/assets")
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset, @PathVariable Integer userId, @PathVariable Integer portfolioId){
        return assetService.createAsset(asset, userId, portfolioId);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}")
    public void deleteAsset(@PathVariable Integer userId, @PathVariable Integer portfolioId, @PathVariable Integer assetId){
        assetService.deleteAsset(userId, portfolioId, assetId);
    }
}
