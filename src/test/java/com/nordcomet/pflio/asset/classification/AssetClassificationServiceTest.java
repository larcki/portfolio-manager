package com.nordcomet.pflio.asset.classification;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class AssetClassificationServiceTest {

    @Autowired
    private AssetRepo assetRepo;

    @Test
    void shouldFindAssetsMatchingClassification() {
        AssetClassificationService underTest = new AssetClassificationService(assetRepo);

        Asset asset = assetRepo.save(randomAssetBuilder()
                .assetClasses(Set.of(new AssetClass(Region.DEVELOPED, AssetClassType.STOCK, BigDecimal.ONE)))
                .build());
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED_STOCK).get(0), is(asset));
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED).get(0), is(asset));
        assertThat(underTest.findAssets(AssetClassification.BOND).isEmpty(), is(true));
    }

    @Test
    void shouldFindAssetsMatchingClassificationWhenMultipleClasses() {
        AssetClassificationService underTest = new AssetClassificationService(assetRepo);

        Asset asset = assetRepo.save(randomAssetBuilder()
                .assetClasses(Set.of(
                        new AssetClass(Region.DEVELOPED, AssetClassType.STOCK, new BigDecimal("0.5")),
                        new AssetClass(Region.EMERGING, AssetClassType.STOCK, new BigDecimal("0.5"))
                ))
                .build());
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED_STOCK).get(0), is(asset));
        assertThat(underTest.findAssets(AssetClassification.EMERGING).get(0), is(asset));
    }

    @Test
    void shouldFindAssetsMatchingClassificationWhenPartialClasses() {
        AssetClassificationService underTest = new AssetClassificationService(assetRepo);

        Asset asset = assetRepo.save(randomAssetBuilder()
                .assetClasses(Set.of(
                        new AssetClass(null, AssetClassType.PROPERTY, BigDecimal.ONE)
                ))
                .build());
        assertThat(underTest.findAssets(AssetClassification.PROPERTY).get(0), is(asset));
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED).isEmpty(), is(true));
    }

}