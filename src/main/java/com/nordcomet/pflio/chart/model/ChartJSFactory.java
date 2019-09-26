package com.nordcomet.pflio.chart.model;

import java.util.Map;

public class ChartJSFactory {

    private static final Map<PortfolioChartType, String> chartTypes = Map.of(
            PortfolioChartType.PERFORMANCE_LINE, "line",
            PortfolioChartType.PIE, "pie",
            PortfolioChartType.STACKED_RELATIVE, "line",
            PortfolioChartType.STACKED_VALUE, "line",
            PortfolioChartType.VALUE_LINE, "line"
    );


    public static ChartJS createChartJS(PortfolioChartType type) {
//        new ChartJS(chartTypes.get(type), new ChartJSData(), )
        return null;
    }
}
