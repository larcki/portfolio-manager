package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssetClassificationService {

    private final AssetRepo assetRepo;

    @Autowired
    public AssetClassificationService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public Map<AssetClassification, List<Asset>> findAssets(List<AssetClassification> assetClassifications) {
        Map<AssetClassification, List<Asset>> result = new LinkedHashMap<>();

        for (AssetClassification classification : assetClassifications) {
            result.put(classification, assetRepo.findAssetsByRegionAndAssetClassesName(classification.getRegion(), classification.getClassType()));
        }

        return result;
    }

}
