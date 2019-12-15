package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetClassificationService {

    private final AssetRepo assetRepo;

    @Autowired
    public AssetClassificationService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public List<Asset> findAssets(AssetClassification assetClassification) {

        if (assetClassification.getAssetClassTypes().isEmpty() && assetClassification.getRegions().isEmpty()) {
            return new ArrayList<>(assetRepo.findAll());
        }
        if (assetClassification.getAssetClassTypes().isEmpty()) {
            return assetRepo.findAssetsByAssetClasses2RegionIn(assetClassification.getRegions());
        }
        if (assetClassification.getRegions().isEmpty()) {
            return assetRepo.findAssetsByAssetClasses2AssetClassTypeIn(assetClassification.getAssetClassTypes());
        }

        return assetRepo.findAssetsByAssetClasses2AssetClassTypeInAndAssetClasses2RegionIn(
                assetClassification.getAssetClassTypes(),
                assetClassification.getRegions());
    }

}
