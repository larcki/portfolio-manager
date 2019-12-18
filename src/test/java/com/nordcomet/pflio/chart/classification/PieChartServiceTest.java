package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClass2;
import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static com.nordcomet.pflio.asset.model.AssetClassType.*;
import static com.nordcomet.pflio.asset.model.Region.*;
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
                new AssetClass2(DEVELOPED, STOCK, new BigDecimal("0.8")),
                new AssetClass2(EMERGING, STOCK, new BigDecimal("0.2"))));

        createAssetWithTransaction("Asset B", "200", Set.of(
                new AssetClass2(DEVELOPED, STOCK, new BigDecimal("0.5")),
                new AssetClass2(null, BOND, new BigDecimal("0.5"))));

        createAssetWithTransaction("Asset C", "700", Set.of(
                new AssetClass2(null, PROPERTY, new BigDecimal("1"))));

        createAssetWithTransaction("Asset D", "300", Set.of(
                new AssetClass2(NORDIC, STOCK, new BigDecimal("1"))));


        Object result = underTest.getPieChart(List.of(
                AssetClassification.DEVELOPED_STOCK,
                AssetClassification.EMERGING_STOCK,
                AssetClassification.BOND,
                AssetClassification.OTHER
        ));

        System.out.println(result);

    }

    private void createAssetWithTransaction(String name, String value, Set<AssetClass2> assetClasses2) {
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

    }

}