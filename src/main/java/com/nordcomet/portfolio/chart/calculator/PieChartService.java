package com.nordcomet.portfolio.chart.calculator;

import com.nordcomet.portfolio.chart.AssetClassification;
import com.nordcomet.portfolio.chart.chartjs.ChartJSFactory;
import com.nordcomet.portfolio.chart.utils.AssetProportionUsageHelper;
import com.nordcomet.portfolio.chart.utils.ColourPalette;
import com.nordcomet.portfolio.service.asset.AssetClassificationService;
import com.nordcomet.portfolio.service.assetposition.AssetPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PieChartService {

    private final AssetClassificationService assetClassificationService;
    private final AssetPositionService assetPositionService;

    @Autowired
    public PieChartService(AssetClassificationService assetClassificationService, AssetPositionService assetPositionService) {
        this.assetClassificationService = assetClassificationService;
        this.assetPositionService = assetPositionService;
    }

    public Object getPieChart(List<AssetClassification> assetClasses) {
        Map<Object, String> colourPalette = ColourPalette.createColourPalette(assetClasses);
        AssetProportionUsageHelper assetProportionUsageHelper = new AssetProportionUsageHelper();

        List<BigDecimal> data = assetClasses.stream()
                .map(classification -> assetClassificationService.findAssets(classification).stream()
                        .map(asset -> {
                            BigDecimal proportionToUse = assetProportionUsageHelper.getProportionToUse(asset, classification);
                            return assetPositionService.getTotalValueOf(asset).multiply(proportionToUse);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add))

                .collect(Collectors.toList());

        return ChartJSFactory.createPieChart(assetClasses, colourPalette, data);
    }
}
