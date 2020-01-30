package com.nordcomet.portfolio.chart.utils;

import com.nordcomet.portfolio.chart.AssetClassification;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetClass;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class AssetProportionUsageHelper {

    private Map<Asset, BigDecimal> assetProportionUsed = new HashMap<>();

    public BigDecimal getProportionToUse(Asset asset, AssetClassification classification) {
        BigDecimal usedProportion = getProportionUsed(asset);
        BigDecimal proportion = getProportionOfAssetThatBelongsToClassification(classification, asset);
        BigDecimal proportionLeft = BigDecimal.ONE.subtract(usedProportion);
        BigDecimal proportionToUse = proportionLeft.min(proportion);
        assetProportionUsed.put(asset, usedProportion.add(proportionToUse));
        return proportionToUse;
    }

    private BigDecimal getProportionUsed(Asset asset) {
        return assetProportionUsed.get(asset) != null ? assetProportionUsed.get(asset) : BigDecimal.ZERO;
    }

    private BigDecimal getProportionOfAssetThatBelongsToClassification(AssetClassification classification, Asset asset) {
        List<AssetClass> matchingClasses = asset.getAssetClasses().stream()
                .filter(classification::includes)
                .collect(toList());
        return matchingClasses.stream().map(AssetClass::getProportion).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
