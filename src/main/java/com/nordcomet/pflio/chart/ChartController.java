package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.chart.service.ChartService;
import com.nordcomet.pflio.chart.service.PerformanceChartCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/api/chart/performance")
    public Object getPerformanceLineChart(@RequestParam Integer period) {
        return performanceChartCalculator.getPerformanceChart(period);
    }

}
