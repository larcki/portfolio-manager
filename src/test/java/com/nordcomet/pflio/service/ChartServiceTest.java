package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tag;
import com.nordcomet.pflio.model.Tags;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.repo.AssetRepo;
import com.nordcomet.pflio.view.ChartDataset;
import com.nordcomet.pflio.view.ChartView;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nordcomet.pflio.DataRandomiser.randomInt;
import static com.nordcomet.pflio.DataRandomiser.randomString;
import static com.nordcomet.pflio.model.Tags.BOND;
import static com.nordcomet.pflio.model.Tags.STOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChartServiceTest {

    private static final LocalDate yesterday = LocalDate.now().minusDays(1);
    private static final LocalDate today = LocalDate.now();
    private static final LocalDateTime yesterdayMidnight = LocalDate.now().minusDays(1).atStartOfDay();
    private static final LocalDateTime now = LocalDateTime.now();

    private final AssetPositionRepo assetPositionRepo = mock(AssetPositionRepo.class);
    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final ChartDaysResolver daysResolver = mock(ChartDaysResolver.class);

    private ChartService underTest = new ChartService(assetPositionRepo, assetRepo, daysResolver);

    @Test
    void getStackedValueChart_shouldReturnDatasetsWithAggregatedPricesForGivenDays() {
        List<Tags> tags = List.of(BOND, STOCK);
        int daysAgoExcluding = 2;
        List<LocalDate> daysReturnedByDaysResolver = List.of(yesterday, today);
        when(daysResolver.resolveDays(daysAgoExcluding)).thenReturn(daysReturnedByDaysResolver);
        Asset bondAsset1 = createAsset(BOND);
        Asset bondAsset2 = createAsset(BOND);

        Asset stockAsset1 = createAsset(STOCK);
        Asset stockAsset2 = createAsset(STOCK);

        when(assetRepo.findAssetsByTagsNameIn(tags)).thenReturn(Set.of(bondAsset1, bondAsset2, stockAsset1, stockAsset2));

        whenAssetPositionsFor(bondAsset1, List.of(
                createAssetPosition(bondAsset1, new BigDecimal("3"), new BigDecimal("15"), yesterdayMidnight.minusDays(9)),
                createAssetPosition(bondAsset1, new BigDecimal("4"), new BigDecimal("20"), now)
        ));

        whenAssetPositionsFor(bondAsset2, List.of(
                createAssetPosition(bondAsset2, new BigDecimal("5"), new BigDecimal("10"), yesterdayMidnight.minusDays(9))
        ));

        whenAssetPositionsFor(stockAsset1, List.of(
                createAssetPosition(stockAsset1, new BigDecimal("2"), new BigDecimal("6"), yesterdayMidnight),
                createAssetPosition(stockAsset1, new BigDecimal("3"), new BigDecimal("9"), now)
        ));

        whenAssetPositionsFor(stockAsset2, List.of(
                createAssetPosition(stockAsset2, new BigDecimal("2"), new BigDecimal("6"), yesterdayMidnight.minusDays(9)),
                createAssetPosition(stockAsset2, new BigDecimal("3"), new BigDecimal("9"), now)
        ));

        ChartView result = underTest.getStackedValueChart(tags, daysAgoExcluding);

        assertThat(result.getTimes().size(), is(daysReturnedByDaysResolver.size()));
        assertThat(result.getChartDatasets().size(), is(tags.size()));
        Optional<ChartDataset> bondDataset = findDataset(result, BOND.name());
        assertThat(bondDataset.get().getData().get(0), is(new BigDecimal("25.0000")));
        assertThat(bondDataset.get().getData().get(1), is(new BigDecimal("30.0000")));
        Optional<ChartDataset> stockDataset = findDataset(result, STOCK.name());
        assertThat(stockDataset.get().getData().get(0), is(new BigDecimal("12.0000")));
        assertThat(stockDataset.get().getData().get(1), is(new BigDecimal("18.0000")));
    }

    @Test
    void getStackedValueChart_shouldReturnDatasetsWithAggregatedPricesForProportionalTags() {
        List<Tags> tags = List.of(BOND, STOCK);
        int daysAgoExcluding = 1;
        List<LocalDate> daysReturnedByDaysResolver = List.of(yesterday, today);
        when(daysResolver.resolveDays(daysAgoExcluding)).thenReturn(daysReturnedByDaysResolver);
        Asset combination_30_70 = createAsset(List.of(new Tag(new BigDecimal("0.3"), BOND), new Tag(new BigDecimal("0.7"), STOCK)));
        Asset combination_90_10 = createAsset(List.of(new Tag(new BigDecimal("0.9"), BOND), new Tag(new BigDecimal("0.1"), STOCK)));
        Asset pureStock = createAsset(List.of(new Tag(new BigDecimal("1"), STOCK)));

        when(assetRepo.findAssetsByTagsNameIn(tags)).thenReturn(Set.of(combination_30_70, combination_90_10, pureStock));

        whenAssetPositionsFor(combination_30_70, List.of(
                createAssetPosition(combination_30_70, new BigDecimal("1"), new BigDecimal("100"), yesterdayMidnight)
        ));
        whenAssetPositionsFor(combination_90_10, List.of(
                createAssetPosition(combination_90_10, new BigDecimal("1"), new BigDecimal("100"), yesterdayMidnight)
        ));
        whenAssetPositionsFor(pureStock, List.of(
                createAssetPosition(pureStock, new BigDecimal("1"), new BigDecimal("100"), yesterdayMidnight)
        ));

        ChartView result = underTest.getStackedValueChart(tags, daysAgoExcluding);

        Optional<ChartDataset> bondDataset = findDataset(result, BOND.name());
        assertThat(bondDataset.get().getData().get(0), is(new BigDecimal("120.0000")));
        Optional<ChartDataset> stockDataset = findDataset(result, STOCK.name());
        assertThat(stockDataset.get().getData().get(0), is(new BigDecimal("180.0000")));
    }

    private Asset createAsset(List<Tag> tags) {
        Asset asset = new Asset();
        asset.setTags(tags);
        asset.setId(randomInt());
        asset.setCode(randomString());
        asset.setName(randomString());
        return asset;
    }

    private Asset createAsset(Tags tag) {
        return createAsset(List.of(new Tag(BigDecimal.ONE, tag)));
    }

    private void whenAssetPositionsFor(Asset bondAsset1, List<AssetPosition> positions) {
        when(assetPositionRepo.findAllByAssetIdAndTimestampAfter(bondAsset1.getId(), yesterdayMidnight.minusDays(10))).thenReturn(positions);
    }

    private Optional<ChartDataset> findDataset(ChartView result, String label) {
        return result.getChartDatasets().stream().filter(chartDataset -> chartDataset.getLabel().equals(label)).findFirst();
    }

    private AssetPosition createAssetPosition(Asset asset, BigDecimal quantity, BigDecimal totalPrice, LocalDateTime timestamp) {
        return new AssetPosition(asset, quantity, BigDecimal.ZERO, totalPrice, timestamp);
    }

}