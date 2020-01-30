package com.nordcomet.portfolio;

import com.nordcomet.portfolio.data.account.AccountRepo;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import com.nordcomet.portfolio.data.fee.FeeRepo;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdateRepo;
import com.nordcomet.portfolio.data.transaction.TransactionRepo;
import com.nordcomet.portfolio.service.transaction.TransactionService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.nordcomet.portfolio.DataRandomiser.*;

@SpringBootTest
@Disabled
//DATASOURCE_URL="jdbc:mysql://localhost:3306/portfolio" DATASOURCE_USERNAME="portfolio" DATASOURCE_PASSWORD="password" ./gradlew test --tests com.nordcomet.portfolio.GenerateTestData.createTestData -i
class GenerateTestData {

    private static final Logger logger = LoggerFactory.getLogger(GenerateTestData.class);

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private FeeRepo feeRepo;

    @Autowired
    private AccountRepo accountRepo;

    void clearDB() {
        priceUpdateRepo.deleteAll();
        transactionRepo.deleteAll();
        assetPositionRepo.deleteAll();
        assetRepo.deleteAll();
        feeRepo.deleteAll();
        accountRepo.deleteAll();
    }

    @Test
    void createTestData() {
        clearDB();
        int daysOfData = 180;

        List<Asset> assets = IntStream.range(1, 10)
                .mapToObj(value -> assetRepo.save(randomAsset()))
                .collect(Collectors.toList());

        IntStream.range(1, daysOfData - 5).forEach(value -> {
            logger.info("Creating data... {}/{}", value, (daysOfData - 5));
            assets.forEach(asset -> {
                if (probabilityOf(0.3)) {
                    transactionService.save(randomTransactionDto(asset, LocalDateTime.now().minusDays(daysOfData).plusDays(value)));
                }
                if (probabilityOf(0.8)) {
                    priceUpdateRepo.save(priceUpdate(asset, daysOfData, value));
                }
            });
        });
    }

}