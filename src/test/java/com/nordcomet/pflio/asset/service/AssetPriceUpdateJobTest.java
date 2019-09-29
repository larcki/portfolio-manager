package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.model.ParserType;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.nordcomet.pflio.DataRandomiser.randomAsset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AssetPriceUpdateJobTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private AssetPriceUpdateJob assetPriceUpdateJob;

    @Test
    void shouldUpdateAssetPricePosition() {
        Asset asset = randomAsset();
        ParserOptions parserOptions = new ParserOptions();
        parserOptions.setParserType(ParserType.MORNINGSTAR_FI);
        parserOptions.setCode("F00000TH8U");
        parserOptions.setTargetCurrency("EUR");
        parserOptions.setSourceCurrency("NOK");
        asset.setParserOptions(parserOptions);
        assetRepo.save(asset);

        assetPriceUpdateJob.updateAssetPrices();

        List<AssetPosition> assetPosition = assetPositionRepo.findAllByAssetId(asset.getId());
        assertThat(assetPosition.size(), is(1));
        System.out.println(assetPosition.get(0));
    }
}