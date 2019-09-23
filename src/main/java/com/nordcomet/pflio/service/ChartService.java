package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.view.ChartDataset;
import com.nordcomet.pflio.view.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    ChartDaysResolver daysResolver;


    public ChartView lineChartFor(int sinceDays, List<Asset> assets) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(assets);

        List<ChartDataset> datasets = new ArrayList<>();
        List<LocalDate> days = daysResolver.resolveDays(sinceDays);
        for (Asset asset : assets) {
            String assetColor = colourPalette.get(asset);
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

            ChartDataset chart = new ChartDataset(asset.getName(), assetColor, values);
            datasets.add(chart);
        }

        return new ChartView(days, datasets);
    }

}
