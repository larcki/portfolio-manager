package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AssetClassificationService {

    private final AssetRepo assetRepo;

    @Autowired
    public AssetClassificationService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public List<Asset> findAssets(List<AssetClassification> assetClassifications) {
        List<Region> regions = assetClassifications.stream()
                .map(AssetClassification::getRegion)
                .distinct()
                .collect(toList());

        List<AssetClassType> assetClassTypes = assetClassifications.stream()
                .map(AssetClassification::getClassType)
                .distinct()
                .collect(toList());

        return assetRepo.findAssetsByRegionInAndAssetClassesNameIn(regions, assetClassTypes);
    }

}
