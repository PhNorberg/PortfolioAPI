package com.philiphiliphilip.myportfolioapi.asset;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @PostMapping("/users/{username}/portfolios/{portfolioname}/assets")
    @PreAuthorize("@usernameTransformer.transform(#username) == authentication.name")
    public ResponseEntity<?> createAsset(@Valid @RequestBody AssetCreationRequest assetCreationRequest,
                                                             @PathVariable String username,
                                                             @PathVariable String portfolioname){
        AssetCreationResponse response = assetService.createAsset(assetCreationRequest, username, portfolioname);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/portfolios/{portfolioId}/{assetId}")
    public void deleteAsset(@PathVariable Integer userId, @PathVariable Integer portfolioId, @PathVariable Integer assetId){
        assetService.deleteAsset(userId, portfolioId, assetId);
    }
}
