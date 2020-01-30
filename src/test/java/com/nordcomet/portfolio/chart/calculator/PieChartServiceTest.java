package com.nordcomet.portfolio.chart.calculator;

import com.nordcomet.portfolio.chart.AssetClassification;
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
import java.util.Set;

import static com.nordcomet.portfolio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.portfolio.data.asset.AssetClassType.*;
import static com.nordcomet.portfolio.data.asset.Region.*;
import static java.math.BigDecimal.ONE;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PieChartServiceTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PieChartService underTest;


    @Test
    void name() {

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


        Object result = underTest.getPieChart(List.of(
                AssetClassification.DEVELOPED_STOCK,
                AssetClassification.EMERGING_STOCK,
                AssetClassification.BOND,
                AssetClassification.OTHER
        ));

        System.out.println(result);

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