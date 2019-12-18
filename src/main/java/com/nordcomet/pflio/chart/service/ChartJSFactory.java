package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.asset.classification.AssetClassification;
import com.nordcomet.pflio.asset.model.AssetClassType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ChartJSFactory {

    public static Map<Object, Object> createPerformanceLineChart(String timeUnit, List<LocalDate> labels, List<BigDecimal> data) {
        return Map.of(
                "type", "line",
                "data", Map.of(
                        "labels", labels,
                        "datasets", List.of(
                                Map.of(
                                        "borderColor", ColourPalette.getOne(),
                                        "backgroundColor", "rgb(0,0,0,0)",
                                        "label", "Total",
                                        "data", data
                                )
                        )
                ),
                "options", createLineChartOptions(timeUnit, false)
        );
    }


    public static Map<Object, Object> createLineChartOptions(String timeUnit, boolean stacked) {
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
                                "stacked", stacked,
                                "scaleLabel", Map.of(
                                        "display", false,
                                        "labelString", "value")))
                )
        );
    }

    public static Map<String, Object> createPieChart(List<AssetClassification> classifications, Map<Object, String> colourPalette, List<BigDecimal> data) {
        return Map.of(
                "type", "pie",
                "data", Map.of(
                        "labels", classifications.stream().map(AssetClassification::getName).collect(toList()),
                        "datasets", List.of(
                                Map.of(
                                        "backgroundColor", classifications.stream().map(colourPalette::get).collect(toList()),
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
