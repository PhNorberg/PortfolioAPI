package com.philiphiliphilip.myportfolioapi.asset;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
