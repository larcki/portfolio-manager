package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AssetClassificationServiceTest {

    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final AssetClassificationService underTest = new AssetClassificationService(assetRepo);

    @Test
    void shouldFindAssetsMatchingClassification() {
        List<AssetClassification> assetClassifications = List.of(
                new AssetClassification(Region.DEVELOPED, AssetClassType.STOCK),
                new AssetClassification(Region.DEVELOPED, AssetClassType.BOND)
        );

        underTest.findAssets(assetClassifications);

        verify(assetRepo).findAssetsByRegionInAndAssetClassesNameIn(List.of(Region.DEVELOPED), List.of(AssetClassType.STOCK, AssetClassType.BOND));
    }
}