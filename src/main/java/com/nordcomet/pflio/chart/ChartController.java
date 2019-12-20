package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.chart.classification.ClassificationProvider;
import com.nordcomet.pflio.chart.classification.ClassifiedChartService;
import com.nordcomet.pflio.chart.classification.PieChartService;
import com.nordcomet.pflio.chart.model.PortfolioChartType;
import com.nordcomet.pflio.chart.performance.PurchaseAmountAndValueChartService;
import com.nordcomet.pflio.chart.service.PerformanceChartCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@Slf4j
public class ChartController {

    @Autowired
    private PieChartService chartService;

    @Autowired
    private PerformanceChartCalculator performanceChartCalculator;

    @Autowired
    private ClassifiedChartService classifiedChartService;

    @Autowired
    private PurchaseAmountAndValueChartService purchaseAmountAndValueChartService;

    @RequestMapping("/api/chart/pie")
    public Object getPieChart() {
        return chartService.getPieChart(ClassificationProvider.defaultClassification());
    }

    @RequestMapping("/api/chart/line")
    public Object getLineChart(@RequestParam Integer period,
                               @RequestParam PortfolioChartType chartType) {

        if (chartType == PortfolioChartType.STACKED_ASSET_CLASS_ALLOCATION) {
            return classifiedChartService.getStackedValueChartFull(ClassificationProvider.defaultClassification(), period);
        }
        if (chartType == PortfolioChartType.LINE_TOTAL_PERFORMANCE_PERCENTAGE) {
            return performanceChartCalculator.getPerformancePercentageChart(period);
        }
        if (chartType == PortfolioChartType.LINE_TOTAL_PURCHASE_AND_VALUE) {
            return purchaseAmountAndValueChartService.createChart(period);
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
