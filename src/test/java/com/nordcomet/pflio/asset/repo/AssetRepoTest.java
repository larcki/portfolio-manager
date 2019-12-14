package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClass;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.pflio.asset.model.AssetClassType.BOND;
import static com.nordcomet.pflio.asset.model.AssetClassType.STOCK;
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
        Asset emergingBond = saveAsset(Region.EMERGING, BOND);

        assertThat(assetRepo.findAssetsByRegionAndAssetClassesName(Region.DEVELOPED, AssetClassType.STOCK),
                contains(developedStock1, developedStock2));

        assertThat(assetRepo.findAssetsByRegionAndAssetClassesName(Region.EMERGING, BOND),
                contains(emergingBond));
    }

    private Asset saveAsset(Region region, AssetClassType assetClassType) {
        Asset emergingStock = randomAssetBuilder().region(region).assetClasses(assetClass(assetClassType)).build();
        return assetRepo.save(emergingStock);
    }

    private Set<AssetClass> assetClass(AssetClassType type) {
        return Set.of(new AssetClass(type, BigDecimal.ONE));
    }
}