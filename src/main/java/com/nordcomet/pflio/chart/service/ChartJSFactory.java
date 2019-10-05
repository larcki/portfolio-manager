package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.PortfolioChartType;

import java.util.List;
import java.util.Map;

public class ChartJSFactory {

    private static final Map<PortfolioChartType, String> chartTypes = Map.of(
            PortfolioChartType.PERFORMANCE_LINE, "line",
            PortfolioChartType.PIE, "pie",
            PortfolioChartType.STACKED_RELATIVE, "line",
            PortfolioChartType.STACKED_VALUE, "line",
            PortfolioChartType.VALUE_LINE, "line"
    );

    public static ChartJS createChartJS(PortfolioChartType type, String timeUnit, ChartJSData chartJSData) {
        return new ChartJS(chartTypes.get(type), chartJSData, createOptions(timeUnit));
    }

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
}
