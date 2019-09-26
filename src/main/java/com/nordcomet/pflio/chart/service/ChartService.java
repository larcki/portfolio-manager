package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Tags;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


@Service
public class ChartService {

    private final AssetPositionRepo assetPositionRepo;
    private final AssetRepo assetRepo;
    private final ChartDaysResolver daysResolver;

    @Autowired
    public ChartService(AssetPositionRepo assetPositionRepo, AssetRepo assetRepo, ChartDaysResolver daysResolver) {
        this.assetPositionRepo = assetPositionRepo;
        this.assetRepo = assetRepo;
        this.daysResolver = daysResolver;
    }

    public ChartJSData lineChartFor(int sinceDays, List<Asset> assets) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(assets);
        List<LocalDate> days = daysResolver.resolveDays(sinceDays);

        List<ChartJSDataset> datasets = new ArrayList<>();
        for (Asset asset : assets) {
            String assetColor = colourPalette.get(asset);
            List<BigDecimal> values = pricesForAsset(days, asset, BigDecimal.ONE);
            ChartJSDataset chart = new ChartJSDataset(asset.getName(), assetColor, values);
            datasets.add(chart);
        }

        return new ChartJSData(days, datasets);
    }

    @Transactional
    public ChartJSData getStackedValueChart(List<Tags> tags, int daysAgoExcluding) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(tags);
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        Set<Asset> assets = assetRepo.findAssetsByTagsNameIn(tags);

        List<ChartJSDataset> datasets = tags.stream().map(tag -> {

            List<List<BigDecimal>> prices = findAssetsForTag(assets, tag).stream()
                    .map(asset -> pricesForAsset(days, asset, asset.getProportionOfTag(tag)))
                    .collect(toList());
            List<BigDecimal> tagsPrices = combinePrices(days, prices);

            return new ChartJSDataset(tag.name(), colourPalette.get(tag), tagsPrices);

        }).collect(toList());

        return new ChartJSData(days, datasets);
    }

    private List<BigDecimal> combinePrices(List<LocalDate> days, List<List<BigDecimal>> prices) {
        List<BigDecimal> tagsPrices = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            BigDecimal daysTotal = BigDecimal.ZERO;
            for (List<BigDecimal> price : prices) {
                daysTotal = daysTotal.add(price.get(i));
            }
            tagsPrices.add(daysTotal);
        }
        return tagsPrices;
    }

    private List<BigDecimal> pricesForAsset(List<LocalDate> days, Asset asset, BigDecimal proportion) {
        List<AssetPosition> assetPositions = assetPositionRepo.findAllByAssetIdAndTimestampAfter(asset.getId(), tenDaysBeforeFirstDate(days));
        return days.stream().map(day -> assetPositions.stream()
                .filter(beforeEndOfDay(day))
                .max(comparing(AssetPosition::getTimestamp))
                .map(assetPosition -> assetPosition.getTotalPrice().multiply(proportion).setScale(4, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO)).collect(toList());
    }

    private Predicate<AssetPosition> beforeEndOfDay(LocalDate day) {
        return it -> it.getTimestamp().isBefore(day.plus(1, ChronoUnit.DAYS).atStartOfDay());
    }

    private LocalDateTime tenDaysBeforeFirstDate(List<LocalDate> days) {
        return days.get(0).minusDays(10).atStartOfDay();
    }

    private Set<Asset> findAssetsForTag(Set<Asset> assets, Tags tag) {
        return assets.stream()
                .filter(asset -> asset.getTags().stream().anyMatch(assetTag -> assetTag.getName() == tag))
                .collect(toSet());
    }
}
