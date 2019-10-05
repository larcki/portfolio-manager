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

    public static ChartJS createChartJS(PortfolioChartType type, String titleText, ChartJSData chartJSData) {
        return new ChartJS(chartTypes.get(type), chartJSData, createOptions(titleText));
    }

    private static Map<Object, Object> createOptions(String titleText) {
        return Map.of(
                "responsive", true,
                "elements", Map.of(
                        "point", Map.of(
                                "radius", 0,
                                "hitRadius", 8)
                ),
                "legend", Map.of(
                        "position", "top"),
                "title", Map.of(
                        "display", false,
                        "text", titleText),
                "scales", Map.of(
                        "xAxes", List.of(Map.of(
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
