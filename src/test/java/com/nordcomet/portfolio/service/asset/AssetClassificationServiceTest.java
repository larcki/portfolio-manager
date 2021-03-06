package com.nordcomet.portfolio.service.asset;

import com.nordcomet.portfolio.chart.AssetClassification;
import com.nordcomet.portfolio.data.asset.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Set;

import static com.nordcomet.portfolio.DataRandomiser.randomAssetBuilder;
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
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED_STOCK), is(Set.of(asset)));
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED), is(Set.of(asset)));
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
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED_STOCK), is(Set.of(asset)));
        assertThat(underTest.findAssets(AssetClassification.EMERGING), is(Set.of(asset)));
    }

    @Test
    void shouldFindAssetsMatchingClassificationWhenPartialClasses() {
        AssetClassificationService underTest = new AssetClassificationService(assetRepo);

        Asset asset = assetRepo.save(randomAssetBuilder()
                .assetClasses(Set.of(
                        new AssetClass(null, AssetClassType.PROPERTY, BigDecimal.ONE)
                ))
                .build());
        assertThat(underTest.findAssets(AssetClassification.PROPERTY), is(Set.of(asset)));
        assertThat(underTest.findAssets(AssetClassification.DEVELOPED).isEmpty(), is(true));
    }

}