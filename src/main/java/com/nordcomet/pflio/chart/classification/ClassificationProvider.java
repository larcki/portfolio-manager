package com.nordcomet.pflio.chart.classification;

import com.nordcomet.pflio.asset.classification.AssetClassification;

import java.util.List;

public class ClassificationProvider {

    public static List<AssetClassification> defaultClassification() {

        return List.of(
                AssetClassification.DEVELOPED_STOCK,
                AssetClassification.EMERGING_STOCK,
                AssetClassification.BOND,
                AssetClassification.PROPERTY,
                AssetClassification.OTHER
        );

    }

}
