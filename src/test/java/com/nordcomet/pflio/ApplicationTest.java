package com.nordcomet.pflio;

import com.nordcomet.pflio.model.*;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.*;
import com.nordcomet.pflio.service.ChartService;
import com.nordcomet.pflio.service.TransactionService;
import com.nordcomet.pflio.view.ChartView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private PriceUpdateRepo priceUpdateRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ChartService chartService;

    void clearDB() {
        priceUpdateRepo.deleteAll();
        transactionRepo.deleteAll();
        assetPositionRepo.deleteAll();
        assetRepo.deleteAll();
        tagRepo.deleteAll();
    }

    @Test
    void setTestDate() {
        clearDB();
        assertThat(assetRepo).isNotNull();

        Asset google = assetRepo.save(ModelCreator.asset("Google", Tags.STOCK));
        Asset amazon = assetRepo.save(ModelCreator.asset("Amazon", Tags.STOCK));
        Asset nokia = assetRepo.save(ModelCreator.asset("Nokia", Tags.STOCK));
        Asset bondIndex = assetRepo.save(ModelCreator.asset("BondIndex", Tags.BOND));

        int daysOfData = 180;

        IntStream.range(1, daysOfData)
                .filter(probability(0.3))
                .forEach(value -> {
                    transactionService.save(ModelCreator.transaction(google, daysOfData, value));
                    transactionService.save(ModelCreator.transaction(amazon, daysOfData, value));
                    transactionService.save(ModelCreator.transaction(nokia, daysOfData, value));
                    transactionService.save(ModelCreator.transaction(bondIndex, daysOfData, value));
                });

        IntStream.range(1, daysOfData)
                .filter(probability(0.8))
                .forEach(value -> {
                    priceUpdateRepo.save(ModelCreator.priceUpdate(google, daysOfData, value));
                    priceUpdateRepo.save(ModelCreator.priceUpdate(amazon, daysOfData, value));
                    priceUpdateRepo.save(ModelCreator.priceUpdate(nokia, daysOfData, value));
                    priceUpdateRepo.save(ModelCreator.priceUpdate(bondIndex, daysOfData, value));
                });
    }

    @Test
    void testAssetPosition() {
        Optional<AssetPosition> latestByAssetId = assetPositionRepo.findFirstByAssetIdAndTimestampBefore(789, LocalDateTime.now());
        System.out.println(latestByAssetId);
    }

    @Test
    void testChartService() {
        Asset asset = assetRepo.findAll().iterator().next();
        ChartView chartView = chartService.lineChartFor(180, asset);
        System.out.println(chartView.getChartDatasets());
    }

    @Test
    void tagSearchTest() {
        List<Tags> tags = List.of(Tags.BOND);
        List<Asset> assets = assetRepo.findAssetsByTagsNameIn(tags);
        System.out.println(assets);
    }

    private IntPredicate probability(double value) {
        return it -> probabilityOf(value);
    }

    private boolean probabilityOf(Double value) {
        return Math.random() <= value;
    }

}