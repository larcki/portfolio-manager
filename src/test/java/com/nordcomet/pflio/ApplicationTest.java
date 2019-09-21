package com.nordcomet.pflio;

import com.nordcomet.pflio.model.*;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.*;
import com.nordcomet.pflio.service.ChartService;
import com.nordcomet.pflio.service.TransactionService;
import com.nordcomet.pflio.view.ChartView;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
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

        Asset google = assetRepo.save(asset("Google", Tags.STOCK));
        Asset amazon = assetRepo.save(asset("Amazon", Tags.STOCK));
        Asset nokia = assetRepo.save(asset("Nokia", Tags.STOCK));
        Asset bondIndex = assetRepo.save(asset("BondIndex", Tags.BOND));

        int daysOfData = 180;

        IntStream.range(1, daysOfData)
                .filter(probability(0.3))
                .forEach(value -> {
                    transactionService.save(transaction(google, daysOfData, value));
                    transactionService.save(transaction(amazon, daysOfData, value));
                    transactionService.save(transaction(nokia, daysOfData, value));
                    transactionService.save(transaction(bondIndex, daysOfData, value));
                });

        IntStream.range(1, daysOfData)
                .filter(probability(0.8))
                .forEach(value -> {
                    priceUpdateRepo.save(priceUpdate(google, daysOfData, value));
                    priceUpdateRepo.save(priceUpdate(amazon, daysOfData, value));
                    priceUpdateRepo.save(priceUpdate(nokia, daysOfData, value));
                    priceUpdateRepo.save(priceUpdate(bondIndex, daysOfData, value));
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

    private PriceUpdate priceUpdate(Asset google, int daysOfData, int value) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setAsset(google);
        priceUpdate.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(value));
        priceUpdate.setPrice(adjust(new BigDecimal("10"), -0.3, 0.4));
        return priceUpdate;
    }

    private BigDecimal adjust(BigDecimal price, double from, double to) {
        return price.add(BigDecimal.valueOf(random(from, to)));
    }

    private double random(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
    }

    private IntPredicate probability(double value) {
        return it -> probabilityOf(value);
    }

    private boolean probabilityOf(Double value) {
        return Math.random() <= value;
    }

    private Transaction transaction(Asset google, int daysOfData, int value) {
        Transaction transaction = new Transaction();
        transaction.setAsset(google);
        transaction.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(value));
        transaction.setPrice(adjust(new BigDecimal("10"), -0.3, 0.4));
        transaction.setQuantityChange(new BigDecimal(randomInt(5, 10)));
        return transaction;
    }

    private int randomInt(int from, int to) {
        return from + new Random().nextInt(to);
    }

    private Asset asset(String name, Tags tag) {
        Asset asset = new Asset();
        asset.setName(name);
        asset.setCode(name);
        asset.setTags(List.of(new Tag(BigDecimal.ONE, tag)));
        return asset;
    }

}