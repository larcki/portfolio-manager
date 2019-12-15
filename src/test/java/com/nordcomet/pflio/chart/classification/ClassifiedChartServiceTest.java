package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClass2;
import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.service.TransactionService;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSDataset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.pflio.asset.model.AssetClassType.*;
import static com.nordcomet.pflio.asset.model.Region.*;
import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClassifiedChartServiceTest {


    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClassifiedChartService underTest;

    @Test
    void name() {
        createAssetWithTransaction("Asset A", "500", Set.of(
                new AssetClass2(DEVELOPED, STOCK, new BigDecimal("0.8")),
                new AssetClass2(EMERGING, STOCK, new BigDecimal("0.2"))));

        createAssetWithTransaction("Asset B", "200", Set.of(
                new AssetClass2(DEVELOPED, STOCK, new BigDecimal("0.5")),
                new AssetClass2(null, BOND, new BigDecimal("0.5"))));

        createAssetWithTransaction("Asset C", "700", Set.of(
                new AssetClass2(null, PROPERTY, new BigDecimal("1"))));

        createAssetWithTransaction("Asset D", "300", Set.of(
                new AssetClass2(NORDIC, STOCK, new BigDecimal("1"))));

        ChartJSData result = underTest.getStackedValueChart(List.of(
                AssetClassification.DEVELOPED_STOCK,
                AssetClassification.EMERGING_STOCK,
                AssetClassification.BOND
        ), 1);

        assertEquals(3, result.getDatasets().size());
        assertClassificationValue(AssetClassification.DEVELOPED_STOCK, new BigDecimal("800.0000"), result);
        assertClassificationValue(AssetClassification.EMERGING_STOCK, new BigDecimal("100.0000"), result);
        assertClassificationValue(AssetClassification.BOND, new BigDecimal("100.0000"), result);
    }

    private void assertClassificationValue(AssetClassification classification, BigDecimal expectedAmount, ChartJSData result) {
        Optional<ChartJSDataset> developedStock = result.getDatasets().stream()
                .filter(chartJSDataset -> chartJSDataset.getLabel().equals(classification.getName()))
                .findFirst();
        assertTrue(developedStock.isPresent());
        assertEquals(expectedAmount, developedStock.get().getData().get(0));
    }

    private Asset createAssetWithTransaction(String name, String value, Set<AssetClass2> assetClasses2) {
        Asset asset = assetRepo.save(randomAssetBuilder()
                .name(name)
                .assetClasses2(assetClasses2)
                .build());

        transactionService.save(TransactionSaveRequest.builder()
                .assetId(asset.getId())
                .timestamp(LocalDateTime.now())
                .quantityChange(ONE)
                .exchangeRate(ONE)
                .totalAmount(Money.of(new BigDecimal(value), Currency.EUR))
                .unitPrice(Money.of(new BigDecimal(value), Currency.EUR))
                .build());

        return asset;
    }

}