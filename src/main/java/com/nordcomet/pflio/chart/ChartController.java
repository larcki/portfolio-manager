package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.chart.service.ChartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChartController {

    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);

    @Autowired
    private ChartService chartService;

    @RequestMapping("/api/chart/pie")
    public Object pieChart(@RequestParam List<AssetClassType> assetClasses) {
        return chartService.getPieChart(assetClasses);
    }


    @RequestMapping("/api/chart/stacked")
    public Object chart(@RequestParam Integer period,
                        @RequestParam List<AssetClassType> assetClasses) {
        return chartService.getStackedValueChartFull(assetClasses, period);
    }


}
