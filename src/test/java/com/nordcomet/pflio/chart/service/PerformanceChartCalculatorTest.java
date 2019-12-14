package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomInt;
import static com.nordcomet.pflio.DataRandomiser.randomString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PerformanceChartCalculatorTest {

    private static final LocalDate yesterday = LocalDate.now().minusDays(1);
    private static final LocalDate today = LocalDate.now();
    private static final LocalDateTime yesterdayMidnight = LocalDate.now().minusDays(1).atStartOfDay();
    private static final LocalDateTime now = LocalDateTime.now();

    private final AssetPositionRepo assetPositionRepo = mock(AssetPositionRepo.class);
    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final ChartDaysResolver daysResolver = mock(ChartDaysResolver.class);

    private PerformanceChartCalculator underTest = new PerformanceChartCalculator(daysResolver, assetRepo, assetPositionRepo);

    @Test
    void getPerformanceLineChart() {
        int daysAgoExcluding = 4;
        List<LocalDate> daysReturnedByDaysResolver = List.of(yesterday.minusDays(2), yesterday.minusDays(1), yesterday, today);
        when(daysResolver.resolveDays(daysAgoExcluding)).thenReturn(daysReturnedByDaysResolver);

        Asset asset1 = createAsset();
        Asset asset2 = createAsset();

        when(assetRepo.findAll()).thenReturn(Set.of(asset1, asset2));

        whenAssetPositionsFor(asset1, List.of(
                createAssetPositionWithTotalPurchase(asset1, new BigDecimal("110"), new BigDecimal("100"), yesterdayMidnight.minusDays(2)),
                createAssetPositionWithTotalPurchase(asset1, new BigDecimal("105"), new BigDecimal("100"), yesterdayMidnight.minusDays(1)),
                createAssetPositionWithTotalPurchase(asset1, new BigDecimal("100"), new BigDecimal("100"), yesterdayMidnight),
                createAssetPositionWithTotalPurchase(asset1, new BigDecimal("220"), new BigDecimal("200"), now)
        ));

        whenAssetPositionsFor(asset2, List.of(
                createAssetPositionWithTotalPurchase(asset2, new BigDecimal("100"), new BigDecimal("100"), yesterdayMidnight.minusDays(2)),
                createAssetPositionWithTotalPurchase(asset2, new BigDecimal("100"), new BigDecimal("100"), yesterdayMidnight.minusDays(1)),
                createAssetPositionWithTotalPurchase(asset2, new BigDecimal("110"), new BigDecimal("100"), yesterdayMidnight),
                createAssetPositionWithTotalPurchase(asset2, new BigDecimal("200"), new BigDecimal("201"), now)
        ));

        List<LocalDate> days = List.of(
                LocalDate.now().minusDays(3),
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(0)
        );

        List<BigDecimal> result = underTest.getPerformanceData(days);

        assertThat(result.get(0), is(new BigDecimal("0.00")));
        assertThat(result.get(1), is(new BigDecimal("-2.38")));
        assertThat(result.get(2), is(new BigDecimal("0.00")));
        assertThat(result.get(3), is(new BigDecimal("4.29")));
    }

    private void whenAssetPositionsFor(Asset bondAsset1, List<AssetPosition> positions) {
        when(assetPositionRepo.findAllByAssetIdAndTimestampAfter(eq(bondAsset1.getId()), any(LocalDateTime.class))).thenReturn(positions);
    }

    private Asset createAsset() {
        return Asset.builder()
                .assetClasses(Set.of(new AssetClass(AssetClassType.STOCK, BigDecimal.ONE)))
                .id(randomInt())
                .name(randomString())
                .baseCurrency(Currency.EUR)
                .quoteCurrency(Currency.EUR)
                .build();
    }

    private AssetPosition createAssetPositionWithTotalPurchase(Asset asset, BigDecimal totalValue, BigDecimal totalPurchaseAmount, LocalDateTime dateTime) {
        return new AssetPosition(asset, BigDecimal.TEN, BigDecimal.ZERO, totalValue, totalPurchaseAmount, dateTime);
    }

}