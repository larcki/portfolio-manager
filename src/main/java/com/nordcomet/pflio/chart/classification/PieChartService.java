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
        Map<Asset, BigDecimal> assetProportionsExhausted = new HashMap<>();

        List<BigDecimal> data = assetClasses.stream()
                .map(classification -> assetClassificationService.findAssets(classification).stream()
                        .map(asset -> {
                            BigDecimal exhaustedProportion = getExhaustedProportion(assetProportionsExhausted, asset);
                            BigDecimal proportionLeft = BigDecimal.ONE.subtract(exhaustedProportion);
                            BigDecimal proportion = proportionThatIncludesClassification(classification, asset);
                            BigDecimal proportionToExhaust = proportionLeft.min(proportion);
                            assetProportionsExhausted.put(asset, exhaustedProportion.add(proportionToExhaust));

                            return assetPositionService.getTotalValueOf(asset).multiply(proportionToExhaust);

                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add))

                .collect(Collectors.toList());

        return ChartJSFactory.createPieChart(assetClasses, colourPalette, data);
    }

    private BigDecimal getExhaustedProportion(Map<Asset, BigDecimal> assetProportionsExhausted, Asset asset) {
        return assetProportionsExhausted.get(asset) != null ? assetProportionsExhausted.get(asset) : BigDecimal.ZERO;
    }

    private BigDecimal proportionThatIncludesClassification(AssetClassification classification, Asset asset) {
        List<AssetClass2> matchingClasses = asset.getAssetClasses2().stream()
                .filter(classification::includes)
                .collect(toList());
        return matchingClasses.stream().map(AssetClass2::getProportion).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
