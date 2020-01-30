package com.nordcomet.portfolio.scheduled;

import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.asset.ParserOptions;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdate;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdateRepo;
import com.nordcomet.portfolio.data.transaction.TransactionRepo;
import com.nordcomet.portfolio.service.priceupdate.imports.parser.ParserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static com.nordcomet.portfolio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.portfolio.DataRandomiser.randomBaseTransaction;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AssetPriceUpdateJobTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Autowired
    private AssetPriceUpdateJob assetPriceUpdateJob;

    @Autowired
    private TransactionRepo transactionRepo;

    @Test
    void shouldCreatePriceUpdate() {
        ParserOptions parserOptions = new ParserOptions();
        parserOptions.setParserType(ParserType.MORNINGSTAR_FUND);
        parserOptions.setCode("F00000TH8U");
        parserOptions.setTargetCurrency("EUR");
        parserOptions.setSourceCurrency("NOK");
        Asset asset = randomAssetBuilder()
                .parserOptions(parserOptions)
                .build();
        assetRepo.save(asset);

        createTransaction(asset);

        assetPriceUpdateJob.updateAssetPrices();

        List<PriceUpdate> assetPosition = priceUpdateRepo.findAllByAssetId(asset.getId());
        assertThat(assetPosition.size(), is(1));
        System.out.println(assetPosition.get(0));
    }

    private void createTransaction(Asset asset_1) {
        transactionRepo.save(randomBaseTransaction(asset_1)
                .timestamp(LocalDateTime.now().minusDays(10))
                .build());
    }
}