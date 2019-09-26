package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.ChartJSOptions;
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

    private static ChartJSOptions createOptions(String titleText) {
        return new ChartJSOptions(Map.of(
                "responsive", true,
                "title", Map.of(
                        "display", true,
                        "text", titleText),
                "scales", Map.of(
                        "xAxes", List.of(Map.of(
                                "scaleLabel", Map.of(
                                        "display", true,
                                        "labelString", "Time"))),
                        "yAxes", List.of(Map.of(
                                "stacked", true,
                                "scaleLabel", Map.of(
                                        "display", true,
                                        "labelString", "value")))

                )
        ));
    }
}
