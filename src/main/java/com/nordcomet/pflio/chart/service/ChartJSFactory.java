package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.asset.model.AssetClassType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ChartJSFactory {

    public static Map<Object, Object> createOptions(String timeUnit) {
        return Map.of(
                "responsive", true,
                "layout", Map.of(
                        "padding", Map.of(
                                "right", 25,
                                "left", 15,
                                "bottom", 15,
                                "top", 0
                        )
                ),
                "elements", Map.of(
                        "point", Map.of(
                                "radius", 0,
                                "hitRadius", 8)
                ),
                "legend", Map.of(
                        "position", "top"),
                "title", Map.of(
                        "display", false,
                        "text", ""),
                "scales", Map.of(
                        "xAxes", List.of(Map.of(
                                "type", "time",
                                "time", Map.of(
                                        "unit", timeUnit
                                ),
                                "scaleLabel", Map.of(
                                        "display", false,
                                        "labelString", "Time"))),
                        "yAxes", List.of(Map.of(
                                "stacked", true,
                                "scaleLabel", Map.of(
                                        "display", false,
                                        "labelString", "value")))
                )
        );
    }

    public static Map<String, Object> createPieChart(List<AssetClassType> assetClasses, Map<Object, String> colourPalette, List<BigDecimal> data) {
        return Map.of(
                "type", "pie",
                "data", Map.of(
                        "labels", assetClasses.stream().map(Enum::name).collect(toList()),
                        "datasets", List.of(
                                Map.of(
                                        "backgroundColor", assetClasses.stream().map(colourPalette::get).collect(toList()),
                                        "data", data
                                )
                        )
                ),
                "options", Map.of(
                        "legend", Map.of(
                                "position", "top"),
                        "layout", Map.of(
                                "padding", Map.of(
                                        "right", 0,
                                        "left", 0,
                                        "bottom", 0,
                                        "top", 0
                                )
                        )
                )
        );
    }
}
