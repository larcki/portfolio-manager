package com.nordcomet.portfolio.controller;

import com.nordcomet.portfolio.chart.PortfolioChartType;
import com.nordcomet.portfolio.chart.calculator.ClassifiedChartService;
import com.nordcomet.portfolio.chart.calculator.PerformanceChartCalculator;
import com.nordcomet.portfolio.chart.calculator.PieChartService;
import com.nordcomet.portfolio.chart.calculator.PurchaseAmountAndValueChartService;
import com.nordcomet.portfolio.chart.utils.ClassificationProvider;
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

    private final PieChartService chartService;
    private final PerformanceChartCalculator performanceChartCalculator;
    private final ClassifiedChartService classifiedChartService;
    private final PurchaseAmountAndValueChartService purchaseAmountAndValueChartService;

    @Autowired
    public ChartController(PieChartService chartService, PerformanceChartCalculator performanceChartCalculator, ClassifiedChartService classifiedChartService, PurchaseAmountAndValueChartService purchaseAmountAndValueChartService) {
        this.chartService = chartService;
        this.performanceChartCalculator = performanceChartCalculator;
        this.classifiedChartService = classifiedChartService;
        this.purchaseAmountAndValueChartService = purchaseAmountAndValueChartService;
    }

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
