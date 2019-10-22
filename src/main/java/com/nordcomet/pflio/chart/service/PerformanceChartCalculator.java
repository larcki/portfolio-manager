package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSDatasetBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.math.BigDecimal.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
public class PerformanceChartCalculator {

    private final ChartDaysResolver daysResolver;
    private final AssetRepo assetRepo;
    private final AssetPositionRepo assetPositionRepo;

    @Autowired
    public PerformanceChartCalculator(ChartDaysResolver daysResolver, AssetRepo assetRepo, AssetPositionRepo assetPositionRepo) {
        this.daysResolver = daysResolver;
        this.assetRepo = assetRepo;
        this.assetPositionRepo = assetPositionRepo;
    }

    public Object getPerformancePercentageChart(int daysAgoExcluding, Integer assetId) {
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        Set<Asset> assets = assetRepo.findAssetsById(assetId).map(Set::of).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        TreeMap<LocalDate, PerformanceCalculationItem> items = getCalculationItems(days, assets);
        List<BigDecimal> data = calculatePerformancePercentage(items);
        return ChartJSFactory.createPerformanceLineChart(daysResolver.resolveTimeUnit(daysAgoExcluding), days, data);
    }

    public Object getPerformanceValueChart(int daysAgoExcluding) {
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        Map<LocalDate, PerformanceCalculationItem> items = getCalculationItems(days, assetRepo.findAll());

        List<BigDecimal> purchaseAmounts = items.entrySet().stream()
                .map(entry -> entry.getValue().getTotalPurchaseAmount())
                .collect(Collectors.toList());

        List<BigDecimal> totalAmounts = items.entrySet().stream()
                .map(entry -> entry.getValue().getTotalValue())
                .collect(Collectors.toList());

        ChartJSData data = new ChartJSData(days, List.of(
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
        String timeUnit = daysResolver.resolveTimeUnit(daysAgoExcluding);
        return new ChartJS("line", data, ChartJSFactory.createLineChartOptions(timeUnit, true));
    }


    public Object getPerformancePercentageChart(int daysAgoExcluding) {
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        List<BigDecimal> data = getPerformanceData(days);
        return ChartJSFactory.createPerformanceLineChart(daysResolver.resolveTimeUnit(daysAgoExcluding), days, data);
    }

    public List<BigDecimal> getPerformanceData(List<LocalDate> days) {
        TreeMap<LocalDate, PerformanceCalculationItem> items = getCalculationItems(days, assetRepo.findAll());
        return calculatePerformancePercentage(items);
    }

    private TreeMap<LocalDate, PerformanceCalculationItem> getCalculationItems(List<LocalDate> days, Set<Asset> allAssets) {
        Map<Integer, List<AssetPosition>> assetPositions = findRelevantAssetPositions(days, allAssets);

        TreeMap<LocalDate, PerformanceCalculationItem> calculationItemsByDay = new TreeMap<>();
        for (LocalDate day : days) {
            PerformanceCalculationItem accumulated = allAssets.stream()
                    .map(asset -> findAssetPositionFor(day, assetPositions.get(asset.getId())))
                    .map(assetPosition -> assetPosition
                            .map(this::toPerformanceCalculationItem)
                            .orElse(zeroItem()))
                    .reduce(zeroItem(), PerformanceCalculationItem::accumulate);
            calculationItemsByDay.put(day, accumulated);
        }

        return calculationItemsByDay;
    }

    private PerformanceCalculationItem zeroItem() {
        return new PerformanceCalculationItem(ZERO, ZERO);
    }

    private List<BigDecimal> calculatePerformancePercentage(TreeMap<LocalDate, PerformanceCalculationItem> calculationItemsByDay) {
        ensureNonZeroItems(calculationItemsByDay);
        PerformanceCalculationItem firstDayPosition = calculationItemsByDay.firstEntry().getValue();
        BigDecimal firstDaysTotalValue = firstDayPosition.getTotalValue();
        BigDecimal firstDayPerformance = firstDaysTotalValue.subtract(firstDayPosition.getTotalPurchaseAmount());

        List<BigDecimal> percentagesForDays = calculationItemsByDay.values().stream()
                .skip(1)
                .map(item -> calculatePerformancePercentage(item, firstDaysTotalValue, firstDayPerformance))
                .collect(toList());

        percentagesForDays.add(0, new BigDecimal("0.00"));
        return percentagesForDays;
    }

    private void ensureNonZeroItems(TreeMap<LocalDate, PerformanceCalculationItem> calculationItemsByDay) {
        PerformanceCalculationItem firstItem = calculationItemsByDay.firstEntry().getValue();
        if (firstItem.getTotalPurchaseAmount().compareTo(ZERO) == 0 && firstItem.getTotalValue().compareTo(ZERO) == 0) {
            PerformanceCalculationItem firstNonZeroItem = null;
            for (Map.Entry<LocalDate, PerformanceCalculationItem> itemEntry : calculationItemsByDay.entrySet()) {
                if (!itemEntry.getValue().isZero()) {
                    firstNonZeroItem = itemEntry.getValue();
                }
            }
            for (Map.Entry<LocalDate, PerformanceCalculationItem> itemEntry : calculationItemsByDay.entrySet()) {
                if (!itemEntry.getValue().isZero()) {
                    return;
                }
                calculationItemsByDay.replace(itemEntry.getKey(), firstNonZeroItem);
            }
        }
    }

    private BigDecimal calculatePerformancePercentage(PerformanceCalculationItem item,
                                                      BigDecimal firstDaysTotalValue,
                                                      BigDecimal firstDayPerformance) {

        BigDecimal currentDaysPerformance = item.getTotalValue().subtract(item.getTotalPurchaseAmount());
        BigDecimal performanceDifference = currentDaysPerformance.subtract(firstDayPerformance);
        return performanceDifference
                .divide(firstDaysTotalValue, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    private Map<Integer, List<AssetPosition>> findRelevantAssetPositions(List<LocalDate> days, Set<Asset> allAssets) {
        return allAssets.stream().collect(
                toMap(Asset::getId,
                        asset -> assetPositionRepo
                                .findAllByAssetIdAndTimestampAfter(asset.getId(), tenDaysBeforeFirstDate(days))));
    }

    private Optional<AssetPosition> findAssetPositionFor(LocalDate day, List<AssetPosition> relevantPositions) {
        return relevantPositions.stream()
                .filter(beforeEndOfDay(day))
                .max(comparing(AssetPosition::getTimestamp));
    }

    private LocalDateTime tenDaysBeforeFirstDate(List<LocalDate> days) {
        return days.get(0).minusDays(10).atStartOfDay();
    }

    private Predicate<AssetPosition> beforeEndOfDay(LocalDate day) {
        return it -> it.getTimestamp().isBefore(day.plus(1, ChronoUnit.DAYS).atStartOfDay());
    }

    private PerformanceCalculationItem toPerformanceCalculationItem(AssetPosition assetPosition) {
        return new PerformanceCalculationItem(assetPosition.getTotalPrice(), assetPosition.getTotalPurchaseAmount());
    }

    public BigDecimal getTotalValue() {
        return assetRepo.findAll().stream()
                .map(asset -> assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())
                        .map(AssetPosition::getTotalPrice)
                        .orElse(ZERO))
                .reduce(ZERO, BigDecimal::add);
    }

    @Data
    @AllArgsConstructor
    class PerformanceCalculationItem {
        private BigDecimal totalValue;
        private BigDecimal totalPurchaseAmount;

        boolean isZero() {
            return this.totalValue.compareTo(BigDecimal.ZERO) == 0 && this.totalPurchaseAmount.compareTo(BigDecimal.ZERO) == 0;
        }

        PerformanceCalculationItem accumulate(PerformanceCalculationItem other) {
            return new PerformanceCalculationItem(this.totalValue.add(other.totalValue), this.totalPurchaseAmount.add(other.totalPurchaseAmount));
        }

    }

}
