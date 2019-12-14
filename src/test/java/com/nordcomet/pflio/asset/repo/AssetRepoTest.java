package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClass;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.pflio.asset.model.AssetClassType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@DataJpaTest
class AssetRepoTest {

    @Autowired
    private AssetRepo assetRepo;

    @Test
    void findAssetsByAssetClassesNameInAndRegionIn_shouldReturnAssets_whenMatchAssetClassAndRegion() {
        Asset developedStock1 = saveAsset(Region.DEVELOPED, STOCK);
        Asset developedStock2 = saveAsset(Region.DEVELOPED, STOCK);
        Asset emergingStock = saveAsset(Region.EMERGING, STOCK);
        Asset emergingBond = saveAsset(Region.EMERGING, BOND);
        Asset worldProperty = saveAsset(Region.WORLD, PROPERTY);
        Asset worldStock = saveAsset(Region.WORLD, STOCK);

        List<Asset> result = assetRepo.findAssetsByRegionInAndAssetClassesNameIn(List.of(Region.DEVELOPED), List.of(AssetClassType.STOCK));
        assertThat(result, contains(developedStock1, developedStock2));

        result = assetRepo.findAssetsByRegionInAndAssetClassesNameIn(List.of(Region.EMERGING), Arrays.asList(AssetClassType.values()));
        assertThat(result, contains(emergingStock, emergingBond));

        result = assetRepo.findAssetsByRegionInAndAssetClassesNameIn(Arrays.asList(Region.values()), List.of(STOCK));
        assertThat(result, contains(developedStock1, developedStock2, emergingStock, worldStock));

        result = assetRepo.findAssetsByRegionInAndAssetClassesNameIn(List.of(Region.WORLD), List.of(PROPERTY, BOND, STOCK));
        assertThat(result, contains(worldProperty, worldStock));
    }

    private Asset saveAsset(Region region, AssetClassType assetClassType) {
        Asset emergingStock = randomAssetBuilder().region(region).assetClasses(assetClass(assetClassType)).build();
        return assetRepo.save(emergingStock);
    }

    private Set<AssetClass> assetClass(AssetClassType type) {
        return Set.of(new AssetClass(type, BigDecimal.ONE));
    }
}