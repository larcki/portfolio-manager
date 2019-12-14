package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.nordcomet.pflio.DataRandomiser.randomAsset;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssetClassificationServiceTest {

    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final AssetClassificationService underTest = new AssetClassificationService(assetRepo);

    @Test
    void shouldFindAssetsMatchingClassification() {
        List<AssetClassification> assetClassifications = List.of(
                new AssetClassification(Region.DEVELOPED, AssetClassType.STOCK),
                new AssetClassification(Region.DEVELOPED, AssetClassType.BOND)
        );

        List<Asset> firstAssets = List.of(randomAsset(), randomAsset());
        when(assetRepo.findAssetsByRegionAndAssetClassesName(Region.DEVELOPED, AssetClassType.STOCK))
                .thenReturn(firstAssets);

        List<Asset> secondAssets = List.of(randomAsset());
        when(assetRepo.findAssetsByRegionAndAssetClassesName(Region.DEVELOPED, AssetClassType.BOND))
                .thenReturn(secondAssets);

        Map<AssetClassification, List<Asset>> result = underTest.findAssets(assetClassifications);

        assertThat(result, hasEntry(new AssetClassification(Region.DEVELOPED, AssetClassType.STOCK), firstAssets));
        assertThat(result, hasEntry(new AssetClassification(Region.DEVELOPED, AssetClassType.BOND), secondAssets));

    }
}