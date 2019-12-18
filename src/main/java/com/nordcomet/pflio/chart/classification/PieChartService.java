package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClass2;
import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.classification.AssetClassificationService;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.service.AssetPositionService;
import com.nordcomet.pflio.chart.service.ChartJSFactory;
import com.nordcomet.pflio.chart.service.ColourPalette;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


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
