package com.nordcomet.pflio.chart.performance;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.service.AssetPositionService;
import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSDataset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static com.nordcomet.pflio.DataRandomiser.randomAssetBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PurchaseAmountAndValueChartServiceTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    @Autowired
    private PurchaseAmountAndValueChartService underTest;

    @Test
    void shouldHaveTotalPurchaseAmountAndValue() {
        createAssetWithPurchaseAmountAndValue(new BigDecimal("100"), new BigDecimal("110"));
        createAssetWithPurchaseAmountAndValue(new BigDecimal("700"), new BigDecimal("750"));
        createAssetWithPurchaseAmountAndValue(new BigDecimal("500"), new BigDecimal("480"));

        Object result = underTest.createChart(1);

        List<ChartJSDataset> datasets = ((ChartJS) result).getData().getDatasets();
        ChartJSDataset totalPurchase = datasets.get(0);
        ChartJSDataset totalValue = datasets.get(1);
        assertThat(totalPurchase.getLabel(), is("Total purchase"));
        assertThat(totalValue.getLabel(), is("Total value"));
        assertThatBigDecimalIs(totalPurchase.getData().get(0), "1300");
        assertThatBigDecimalIs(totalValue.getData().get(0), "1340");
    }

    private void assertThatBigDecimalIs(BigDecimal actual, String expected) {
        assertThat(actual.setScale(0, RoundingMode.HALF_UP).toString(), is(expected));
    }

    private void createAssetWithPurchaseAmountAndValue(BigDecimal totalPurchaseAmount, BigDecimal totalValue) {
        Asset asset = assetRepo.save(randomAssetBuilder().build());

        assetPositionService.save(new AssetPosition(
                asset,
                new BigDecimal("1"),
                new BigDecimal("1"),
                totalValue,
                totalPurchaseAmount,
                LocalDateTime.now()
        ));
    }

}