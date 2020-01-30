package com.nordcomet.portfolio.chart;

import com.nordcomet.portfolio.data.asset.AssetClass;
import com.nordcomet.portfolio.data.asset.AssetClassType;
import com.nordcomet.portfolio.data.asset.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.nordcomet.portfolio.data.asset.Region.*;

@AllArgsConstructor
@Getter
public enum AssetClassification {

    DEVELOPED_STOCK("Developed stock", developed(), List.of(AssetClassType.STOCK)),
    EMERGING_STOCK("Emerging stock", emerging(), List.of(AssetClassType.STOCK)),
    BOND("Bond", List.of(), List.of(AssetClassType.BOND)),
    STOCK("Stock", List.of(), List.of(AssetClassType.STOCK)),
    PROPERTY("Property", List.of(), List.of(AssetClassType.PROPERTY)),
    DEVELOPED("Developed", developed(), List.of()),
    EMERGING("Emerging", emerging(), List.of()),
    OTHER("Other", List.of(), List.of()),
    ;

    private final String name;
    private final List<Region> regions;
    private final List<AssetClassType> assetClassTypes;

    public boolean includes(AssetClass assetClass) {
        boolean regionMatch = regions.isEmpty() || regions.stream().anyMatch(region -> assetClass.getRegion() == region);
        boolean assetClassTypeMatch = assetClassTypes.isEmpty() || assetClassTypes.stream().anyMatch(classType -> assetClass.getAssetClassType() == classType);
        return regionMatch && assetClassTypeMatch;
    }

    private static List<Region> emerging() {
        return List.of(Region.EMERGING, FRONTIER, EMERGING_EUROPE, ASIA, CHINA);
    }

    private static List<Region> developed() {
        return List.of(Region.DEVELOPED, NORDIC, EUROPE, EURO_ZONE, WEST_EUROPE, UK, USA, FINLAND, JAPAN, AUSTRALASIA, DEVELOPED_EXCLUDING_UK);
    }

}
