package com.nordcomet.pflio;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.*;
import com.nordcomet.pflio.asset.service.TransactionService;
import com.nordcomet.pflio.chart.service.ChartService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.nordcomet.pflio.DataRandomiser.*;

@SpringBootTest
@Disabled
class GenerateTestData {

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
        int daysOfData = 180;

        List<Asset> assets = IntStream.range(1, 10)
                .mapToObj(value -> assetRepo.save(randomAsset()))
                .collect(Collectors.toList());

        List<Asset> assetsWithPriceParser = createAssetsWithPriceParser().stream()
                .map(asset -> assetRepo.save(asset))
                .collect(Collectors.toList());

        assets.addAll(assetsWithPriceParser);

        IntStream.range(1, daysOfData - 5).forEach(value -> assets.forEach(asset -> {
            if (probabilityOf(0.3)) {
                transactionService.save(transaction(asset, daysOfData, value));
            }
            if (probabilityOf(0.8)) {
                priceUpdateRepo.save(priceUpdate(asset, daysOfData, value));
            }
        }));
    }

    @Test
    void testAssetPosition() {
        Optional<AssetPosition> latestByAssetId = assetPositionRepo.findFirstByAssetIdAndTimestampBefore(789, LocalDateTime.now());
        System.out.println(latestByAssetId);
    }

    @Test
    void tagSearchTest() {
        List<Tags> tags = List.of(Tags.BOND);
        Set<Asset> assets = assetRepo.findAssetsByTagsNameIn(tags);
        System.out.println(assets);
    }

    private static List<Asset> createAssetsWithPriceParser() {
        Asset superFondetNorge = new Asset();
        superFondetNorge.setParserOptions(new ParserOptions(ParserType.MORNINGSTAR_FI, "F00000TH8U", "NOK", "EUR"));
        superFondetNorge.setName("Nordnet Superfondet Norge");
        superFondetNorge.setTags(List.of(new Tag(BigDecimal.ONE, Tags.TMP_ASSETS_WITH_PARSER)));
        return List.of(superFondetNorge);
    }

}