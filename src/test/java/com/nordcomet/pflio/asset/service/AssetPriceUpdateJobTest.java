package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.model.ParserType;
import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static com.nordcomet.pflio.DataRandomiser.randomAsset;
import static com.nordcomet.pflio.DataRandomiser.randomTransaction;
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

    @Autowired
    private TransactionRepo transactionRepo;

    @Test
    void shouldUpdateAssetPricePosition() {
        Asset asset = randomAsset();
        ParserOptions parserOptions = new ParserOptions();
        parserOptions.setParserType(ParserType.MORNINGSTAR_FUND);
        parserOptions.setCode("F00000TH8U");
        parserOptions.setTargetCurrency("EUR");
        parserOptions.setSourceCurrency("NOK");
        asset.setParserOptions(parserOptions);
        assetRepo.save(asset);

        createTransaction(asset);

        assetPriceUpdateJob.updateAssetPrices();

        List<AssetPosition> assetPosition = assetPositionRepo.findAllByAssetId(asset.getId());
        assertThat(assetPosition.size(), is(1));
        System.out.println(assetPosition.get(0));
    }

    private void createTransaction(Asset asset_1) {
        Transaction transaction_1 = randomTransaction(asset_1);
        transaction_1.setTimestamp(LocalDateTime.now().minusDays(10));
        transactionRepo.save(transaction_1);
    }
}