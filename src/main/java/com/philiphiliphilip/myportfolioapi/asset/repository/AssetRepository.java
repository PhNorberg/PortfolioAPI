package com.philiphiliphilip.myportfolioapi.asset.repository;

import com.philiphiliphilip.myportfolioapi.asset.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
}
