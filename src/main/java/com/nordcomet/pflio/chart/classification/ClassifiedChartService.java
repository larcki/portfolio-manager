package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.classification.AssetClassificationService;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.service.AssetPositionService;
import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSDataset;
import com.nordcomet.pflio.chart.model.ChartJSDatasetBuilder;
import com.nordcomet.pflio.chart.service.ChartDaysResolver;
import com.nordcomet.pflio.chart.service.ChartJSFactory;
import com.nordcomet.pflio.chart.service.ColourPalette;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ClassifiedChartService {

    private final ChartDaysResolver daysResolver;
    private final AssetClassificationService assetClassificationService;
    private final AssetPositionService assetPositionService;

    @Autowired
    public ClassifiedChartService(ChartDaysResolver daysResolver, AssetClassificationService assetClassificationService, AssetPositionService assetPositionService) {
        this.daysResolver = daysResolver;
        this.assetClassificationService = assetClassificationService;
        this.assetPositionService = assetPositionService;
    }

    public ChartJS getStackedValueChartFull(List<AssetClassification> classifications, int daysAgoExcluding) {
        ChartJSData data = getStackedValueChart(classifications, daysAgoExcluding);
        String timeUnit = daysResolver.resolveTimeUnit(daysAgoExcluding);
        return new ChartJS("line", data, ChartJSFactory.createLineChartOptions(timeUnit, true));
    }

    @Transactional
    protected ChartJSData getStackedValueChart(List<AssetClassification> classifications, int daysAgoExcluding) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(classifications);
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        AssetProportionUsageHelper assetProportionUsageHelper = new AssetProportionUsageHelper();

        List<ChartJSDataset> datasets = classifications.stream().map(classification -> {

            List<List<BigDecimal>> prices = assetClassificationService.findAssets(classification).stream()
                    .map(asset -> {
                        BigDecimal proportionToUse = assetProportionUsageHelper.getProportionToUse(asset, classification);
                        return pricesForAsset(days, asset, proportionToUse);
                    })
                    .collect(toList());

            List<BigDecimal> tagsPrices = combinePrices(days, prices);

            return new ChartJSDatasetBuilder()
                    .setLabel(classification.getName())
                    .setBackgroundColor(colourPalette.get(classification))
                    .setData(tagsPrices)
                    .setFill(true)
                    .build();

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
        List<AssetPosition> assetPositions = assetPositionService.findAssetPositionsForAssetStartingFrom(asset, days.get(0));

        return days.stream().map(day -> assetPositions.stream()
                .filter(beforeEndOfDay(day))
                .max(comparing(AssetPosition::getTimestamp))
                .map(assetPosition -> priceForAsset(proportion, assetPosition))
                .orElse(BigDecimal.ZERO)).collect(toList());
    }

    private BigDecimal priceForAsset(BigDecimal proportion, AssetPosition assetPosition) {
        return assetPosition.getTotalValue().multiply(proportion).setScale(4, RoundingMode.HALF_UP);
    }

    private Predicate<AssetPosition> beforeEndOfDay(LocalDate day) {
        return it -> it.getTimestamp().isBefore(day.plus(1, ChronoUnit.DAYS).atStartOfDay());
    }

}
