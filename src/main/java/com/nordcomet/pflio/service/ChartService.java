package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tags;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.repo.AssetRepo;
import com.nordcomet.pflio.view.ChartDataset;
import com.nordcomet.pflio.view.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ChartService {

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private ChartDaysResolver daysResolver;


    public ChartView lineChartFor(int sinceDays, List<Asset> assets) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(assets);
        List<LocalDate> days = daysResolver.resolveDays(sinceDays);

        List<ChartDataset> datasets = new ArrayList<>();
        for (Asset asset : assets) {
            String assetColor = colourPalette.get(asset);
            List<BigDecimal> values = pricesForAsset(days, asset, BigDecimal.ONE);
            ChartDataset chart = new ChartDataset(asset.getName(), assetColor, values);
            datasets.add(chart);
        }

        return new ChartView(days, datasets);
    }

    @Transactional
    public ChartView getStackedValueChart(List<Tags> tags, int daysAgoExcluding) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(tags);
        List<LocalDate> days = daysResolver.resolveDays(daysAgoExcluding);
        Set<Asset> assets = assetRepo.findAssetsByTagsNameIn(tags);

        List<ChartDataset> datasets = new ArrayList<>();
        for (Tags tag : tags) {
            Set<Asset> assetsForTag = findAssetsForTag(assets, tag);
            List<List<BigDecimal>> prices = assetsForTag.stream()
                    .map(asset -> pricesForAsset(days, asset, asset.getProportionOfTag(tag)))
                    .collect(Collectors.toList());

            List<BigDecimal> tagsPrices = new ArrayList<>();
            for (int i = 0; i < days.size(); i++) {
                BigDecimal daysTotal = BigDecimal.ZERO;
                for (List<BigDecimal> price : prices) {
                    daysTotal = daysTotal.add(price.get(i));
                }
                tagsPrices.add(daysTotal);
            }
            datasets.add(new ChartDataset(tag.name(), colourPalette.get(tag), tagsPrices));
        }

        return new ChartView(days, datasets);
    }

    private List<BigDecimal> pricesForAsset(List<LocalDate> days, Asset asset, BigDecimal proportion) {
        List<AssetPosition> assetPositions = assetPositionRepo.findAllByAssetIdAndTimestampAfter(asset.getId(), days.get(0).atStartOfDay());
        List<BigDecimal> values = new ArrayList<>();
        for (LocalDate day : days) {
            BigDecimal totalPrice = assetPositions.stream()
                    .filter(it -> !it.getTimestamp().isAfter(day.atStartOfDay()))
                    .max(Comparator.comparing(AssetPosition::getTimestamp))
                    .map(AssetPosition::getTotalPrice)
                    .orElse(BigDecimal.ZERO);
            values.add(totalPrice);
        }
        return values;
    }

    private Set<Asset> findAssetsForTag(Set<Asset> assets, Tags tag) {
        return assets.stream()
                .filter(asset -> asset.getTags().stream().anyMatch(assetTag -> assetTag.getName() == tag))
                .collect(Collectors.toSet());
    }
}
