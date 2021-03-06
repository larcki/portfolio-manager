package com.nordcomet.portfolio.service.priceupdate;

import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdate;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdateRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.nordcomet.portfolio.DataRandomiser.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PriceUpdateServiceTest {

    @Autowired
    private PriceUpdateService underTest;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Test
    void save_shouldCreatePriceUpdateAndNoAssetPosition_whenNoPreviousAssetPositions() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        PriceUpdate priceUpdate = randomPriceUpdate(asset);
        underTest.save(priceUpdate);

        Iterable<PriceUpdate> priceUpdates = priceUpdateRepo.findAll();
        assertThat(priceUpdates.iterator().next().getId(), is(priceUpdate.getId()));
        List<AssetPosition> positions = assetPositionRepo.findAllByAssetId(asset.getId());
        assertThat(positions.size(), is(0));
    }

    @Test
    void save_shouldCreatePriceUpdateAndAssetPosition_whenPreviousAssetPositionExists() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        AssetPosition previousAssetPosition = randomAssetPosition(asset);
        assetPositionRepo.save(previousAssetPosition);

        PriceUpdate priceUpdate = randomPriceUpdate(asset);
        underTest.save(priceUpdate);

        Iterable<PriceUpdate> priceUpdates = priceUpdateRepo.findAll();
        assertThat(priceUpdates.iterator().next().getId(), is(priceUpdate.getId()));

        Optional<AssetPosition> result = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(result.isPresent(), is(true));
        AssetPosition assetPosition = result.get();
        BigDecimal expectedTotalPrice = previousAssetPosition.getQuantity().multiply(priceUpdate.getPrice()).setScale(4, RoundingMode.HALF_UP);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getUnitPrice(), is(priceUpdate.getPrice()));
        assertThat(assetPosition.getQuantity(), is(previousAssetPosition.getQuantity()));
        assertThat(assetPosition.getTotalValue(), is(expectedTotalPrice));
        assertThat(assetPosition.getTotalPurchaseAmount(), is(previousAssetPosition.getTotalPurchaseAmount()));
    }
}