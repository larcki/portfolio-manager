package com.nordcomet.pflio.chart.performance;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.service.AssetPositionService;
import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSDatasetBuilder;
import com.nordcomet.pflio.chart.service.ChartDaysResolver;
import com.nordcomet.pflio.chart.service.ChartJSFactory;
import com.nordcomet.pflio.chart.service.ColourPalette;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

@Service
public class PurchaseAmountAndValueChartService {

    private final ChartDaysResolver daysResolver;
    private final AssetRepo assetRepo;
    private final AssetPositionService assetPositionService;

    @Autowired
    public PurchaseAmountAndValueChartService(ChartDaysResolver daysResolver, AssetRepo assetRepo, AssetPositionService assetPositionService) {
        this.daysResolver = daysResolver;
        this.assetRepo = assetRepo;
        this.assetPositionService = assetPositionService;
    }

    public Object createChart(int daysAgoExcluding) {
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        Set<Asset> allAssets = assetRepo.findAll();
        Map<Integer, List<AssetPosition>> assetPositions = findRelevantAssetPositions(days, allAssets);
        TreeMap<LocalDate, TotalPurchaseAndValueItem> purchaseAndValuePerDay = calculatePurchaseAmountAndValuePerDay(days, allAssets, assetPositions);

        List<BigDecimal> purchaseAmounts = purchaseAndValuePerDay.entrySet().stream()
                .map(entry -> entry.getValue().getTotalPurchaseAmount())
                .collect(Collectors.toList());

        List<BigDecimal> totalAmounts = purchaseAndValuePerDay.entrySet().stream()
                .map(entry -> entry.getValue().getTotalValue())
                .collect(Collectors.toList());

        ChartJSData data = createChartJS(days, purchaseAmounts, totalAmounts);
        String timeUnit = daysResolver.resolveTimeUnit(daysAgoExcluding);
        return new ChartJS("line", data, ChartJSFactory.createLineChartOptions(timeUnit, false));
    }

    private ChartJSData createChartJS(List<LocalDate> days, List<BigDecimal> purchaseAmounts, List<BigDecimal> totalAmounts) {
        return new ChartJSData(days, List.of(
                    new ChartJSDatasetBuilder()
                            .setLabel("Total purchase")
                            .setColor(ColourPalette.getOne())
                            .setBackgroundColor(ColourPalette.getTransparent())
                            .setData(purchaseAmounts)
                            .build(),

                    new ChartJSDatasetBuilder()
                            .setLabel("Total value")
                            .setColor(ColourPalette.getSecond())
                            .setBackgroundColor(ColourPalette.getTransparent())
                            .setData(totalAmounts)
                            .build()
            ));
    }

    private Map<Integer, List<AssetPosition>> findRelevantAssetPositions(List<LocalDate> days, Set<Asset> allAssets) {
        return allAssets.stream().collect(toMap(Asset::getId, asset -> assetPositionService.findAssetPositionsForAssetStartingFrom(asset, days.get(0))));
    }

    private TreeMap<LocalDate, TotalPurchaseAndValueItem> calculatePurchaseAmountAndValuePerDay(List<LocalDate> days, Set<Asset> allAssets, Map<Integer, List<AssetPosition>> assetPositions) {
        TreeMap<LocalDate, TotalPurchaseAndValueItem> purchaseAndValuePerDay = new TreeMap<>();

        for (LocalDate day : days) {
            TotalPurchaseAndValueItem accumulated = allAssets.stream()
                    .map(asset -> getAssetPosition(assetPositions, day, asset))
                    .map(this::createTotalPurchaseAndValueItem)
                    .reduce(zeroPurchaseAndValue(), TotalPurchaseAndValueItem::accumulate);

            purchaseAndValuePerDay.put(day, accumulated);
        }
        return purchaseAndValuePerDay;
    }

    private TotalPurchaseAndValueItem createTotalPurchaseAndValueItem(Optional<AssetPosition> assetPosition) {
        return assetPosition
                .map(this::toPerformanceCalculationItem)
                .orElse(zeroPurchaseAndValue());
    }

    private Optional<AssetPosition> getAssetPosition(Map<Integer, List<AssetPosition>> assetPositions, LocalDate day, Asset asset) {
        return assetPositions.get(asset.getId()).stream()
                .filter(beforeEndOfDay(day))
                .max(comparing(AssetPosition::getTimestamp));
    }

    private TotalPurchaseAndValueItem zeroPurchaseAndValue() {
        return new TotalPurchaseAndValueItem(ZERO, ZERO);
    }

    private Predicate<AssetPosition> beforeEndOfDay(LocalDate day) {
        return it -> it.getTimestamp().isBefore(day.plus(1, ChronoUnit.DAYS).atStartOfDay());
    }

    private TotalPurchaseAndValueItem toPerformanceCalculationItem(AssetPosition assetPosition) {
        return new TotalPurchaseAndValueItem(assetPosition.getTotalValue(), assetPosition.getTotalPurchaseAmount());
    }

    @Data
    @AllArgsConstructor
    class TotalPurchaseAndValueItem {
        private BigDecimal totalValue;
        private BigDecimal totalPurchaseAmount;

        TotalPurchaseAndValueItem accumulate(TotalPurchaseAndValueItem other) {
            return new TotalPurchaseAndValueItem(this.totalValue.add(other.totalValue), this.totalPurchaseAmount.add(other.totalPurchaseAmount));
        }

    }

}
