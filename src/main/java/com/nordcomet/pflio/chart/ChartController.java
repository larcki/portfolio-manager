package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.chart.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private AssetRepo assetRepo;

    @RequestMapping("/api/chart/pie")
    public Object pieChart(@RequestParam List<AssetClassType> assetClasses) {
        return chartService.getPieChart(assetClasses);
    }


    @RequestMapping("/api/chart/stacked")
    public Object chart(@RequestParam Integer period,
                        @RequestParam List<AssetClassType> assetClasses) {
        return chartService.getStackedValueChartFull(assetClasses, period);
    }

    @RequestMapping("/api/assets")
    public List<Object> getAssets() {
        return assetRepo.findAll().stream().map(asset -> Map.of(
                "name", asset.getName(),
                "value", BigDecimal.TEN,
                "profit", BigDecimal.TEN,
                "profitPercentage", BigDecimal.TEN
        )).collect(Collectors.toList());
    }

}
