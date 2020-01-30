package com.nordcomet.portfolio.service.asset;

import com.nordcomet.portfolio.chart.AssetClassification;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AssetClassificationService {

    private final AssetRepo assetRepo;

    @Autowired
    public AssetClassificationService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public Set<Asset> findAssets(AssetClassification assetClassification) {

        if (assetClassification.getAssetClassTypes().isEmpty() && assetClassification.getRegions().isEmpty()) {
            return assetRepo.findAll();
        }
        if (assetClassification.getAssetClassTypes().isEmpty()) {
            return assetRepo.findAssetsByAssetClassesRegionIn(assetClassification.getRegions());
        }
        if (assetClassification.getRegions().isEmpty()) {
            return assetRepo.findAssetsByAssetClassesAssetClassTypeIn(assetClassification.getAssetClassTypes());
        }

        return assetRepo.findAssetsByAssetClassesAssetClassTypeInAndAssetClassesRegionIn(
                assetClassification.getAssetClassTypes(),
                assetClassification.getRegions());
    }

}
