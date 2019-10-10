package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.chart.model.PortfolioChartType;
import com.nordcomet.pflio.chart.service.ChartService;
import com.nordcomet.pflio.chart.service.PerformanceChartCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private PerformanceChartCalculator performanceChartCalculator;

    @RequestMapping("/api/chart/pie")
    public Object getPieChart(@RequestParam List<AssetClassType> assetClasses) {
        return chartService.getPieChart(assetClasses);
    }

    @RequestMapping("/api/chart/stacked")
    public Object getStackedChart(@RequestParam Integer period,
                                  @RequestParam List<AssetClassType> assetClasses) {
        return chartService.getStackedValueChartFull(assetClasses, period);
    }

    @RequestMapping("/api/chart/line")
    public Object getLineChart(@RequestParam Integer period,
                               @RequestParam PortfolioChartType chartType) {

        if (chartType == PortfolioChartType.STACKED_ASSET_CLASS_ALLOCATION) {
            return chartService.getStackedValueChartFull(List.of(AssetClassType.values()), period);
        }
        if (chartType == PortfolioChartType.LINE_TOTAL_PERFORMANCE_PERCENTAGE) {
            return performanceChartCalculator.getPerformancePercentageChart(period);
        }
        if (chartType == PortfolioChartType.LINE_TOTAL_PERFORMANCE) {
            return performanceChartCalculator.getPerformanceValueChart(period);
        }

        return null;

    }

    @RequestMapping("/api/total-amount")
    public BigDecimal getTotalAmountInfo() {
        return performanceChartCalculator.getTotalValue().setScale(2, RoundingMode.HALF_UP);
    }

    @RequestMapping("/api/chart/performance")
    public Object getPerformanceLineChart(@RequestParam Integer period,
                                          @RequestParam(required = false) Integer assetId) {
        if (assetId != null) {
            return performanceChartCalculator.getPerformancePercentageChart(period, assetId);
        }
        return performanceChartCalculator.getPerformancePercentageChart(period);
    }

}
