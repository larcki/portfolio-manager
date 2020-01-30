package com.nordcomet.portfolio.chart.calculator;

import com.nordcomet.portfolio.chart.AssetClassification;
import com.nordcomet.portfolio.chart.chartjs.ChartJSData;
import com.nordcomet.portfolio.chart.chartjs.ChartJSDataset;
import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetClass;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import com.nordcomet.portfolio.service.transaction.TransactionService;
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

import static com.nordcomet.portfolio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.portfolio.data.asset.AssetClassType.*;
import static com.nordcomet.portfolio.data.asset.Region.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
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
    void chartShouldContainClassifications() {
        createAssetWithTransaction("Asset A", "500", Set.of(
                new AssetClass(DEVELOPED, STOCK, new BigDecimal("0.8")),
                new AssetClass(EMERGING, STOCK, new BigDecimal("0.2"))));

        createAssetWithTransaction("Asset B", "200", Set.of(
                new AssetClass(DEVELOPED, STOCK, new BigDecimal("0.5")),
                new AssetClass(null, BOND, new BigDecimal("0.5"))));

        createAssetWithTransaction("Asset C", "700", Set.of(
                new AssetClass(null, PROPERTY, new BigDecimal("1"))));

        createAssetWithTransaction("Asset D", "300", Set.of(
                new AssetClass(NORDIC, STOCK, new BigDecimal("1"))));

        // case 1
        ChartJSData result = underTest.getStackedValueChart(List.of(
                AssetClassification.DEVELOPED_STOCK,
                AssetClassification.EMERGING_STOCK,
                AssetClassification.BOND,
                AssetClassification.OTHER
        ), 1);

        assertEquals(4, result.getDatasets().size());
        assertChartContains(AssetClassification.DEVELOPED_STOCK, new BigDecimal("800.0000"), result);
        assertChartContains(AssetClassification.EMERGING_STOCK, new BigDecimal("100.0000"), result);
        assertChartContains(AssetClassification.BOND, new BigDecimal("100.0000"), result);
        assertChartContains(AssetClassification.OTHER, new BigDecimal("700.0000"), result);
        assertTotalAmount(new BigDecimal("1700.0000"), result);

        // case 2
        result = underTest.getStackedValueChart(List.of(
                AssetClassification.STOCK,
                AssetClassification.BOND,
                AssetClassification.PROPERTY,
                AssetClassification.OTHER
        ), 1);

        assertEquals(4, result.getDatasets().size());
        assertChartContains(AssetClassification.STOCK, new BigDecimal("900.0000"), result);
        assertChartContains(AssetClassification.BOND, new BigDecimal("100.0000"), result);
        assertChartContains(AssetClassification.PROPERTY, new BigDecimal("700.0000"), result);
        assertChartContains(AssetClassification.OTHER, new BigDecimal("0.0000"), result);
        assertTotalAmount(new BigDecimal("1700.0000"), result);

        // case 3
        result = underTest.getStackedValueChart(List.of(
                AssetClassification.DEVELOPED,
                AssetClassification.EMERGING,
                AssetClassification.OTHER
        ), 1);

        assertEquals(3, result.getDatasets().size());
        assertChartContains(AssetClassification.DEVELOPED, new BigDecimal("800.0000"), result);
        assertChartContains(AssetClassification.EMERGING, new BigDecimal("100.0000"), result);
        assertChartContains(AssetClassification.OTHER, new BigDecimal("800.0000"), result);
        assertTotalAmount(new BigDecimal("1700.0000"), result);

    }

    private void assertTotalAmount(BigDecimal expected, ChartJSData result) {
        BigDecimal total = result.getDatasets().stream().map(chartJSDataset -> chartJSDataset.getData().get(0)).reduce(ZERO, BigDecimal::add);
        assertEquals(expected, total);
    }

    private void assertChartContains(AssetClassification classification, BigDecimal expectedAmount, ChartJSData chart) {
        Optional<ChartJSDataset> developedStock = chart.getDatasets().stream()
                .filter(chartJSDataset -> chartJSDataset.getLabel().equals(classification.getName()))
                .findFirst();
        assertTrue(developedStock.isPresent());
        assertEquals(expectedAmount, developedStock.get().getData().get(0));
    }

    private void createAssetWithTransaction(String name, String value, Set<AssetClass> assetClasses2) {
        Asset asset = assetRepo.save(randomAssetBuilder()
                .name(name)
                .assetClasses(assetClasses2)
                .build());

        transactionService.save(TransactionSaveRequest.builder()
                .assetId(asset.getId())
                .timestamp(LocalDateTime.now())
                .quantityChange(ONE)
                .exchangeRate(ONE)
                .totalAmount(Money.of(new BigDecimal(value), Currency.EUR))
                .unitPrice(Money.of(new BigDecimal(value), Currency.EUR))
                .build());

    }

}