package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClass;
import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.model.Asset;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

class AssetProportionUsageHelper {

    private Map<Asset, BigDecimal> assetProportionUsed = new HashMap<>();

    BigDecimal getProportionToUse(Asset asset, AssetClassification classification) {
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
