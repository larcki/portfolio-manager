package com.nordcomet.pflio;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Tags;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.chart.service.ChartService;
import com.nordcomet.pflio.chart.model.ChartView;
import com.nordcomet.pflio.chart.model.PortfolioChartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PortfolioController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private AssetRepo assetRepo;

    @RequestMapping("/")
    public String chart(@RequestParam Integer period,
                        @RequestParam PortfolioChartType chartType,
                        @RequestParam(required = false) List<Tags> tags,
                        @RequestParam(required = false) List<Integer> assets,
                        Model model) {

        Set<Asset> all = assetRepo.findAssetsByTagsNameIn(tags);

        ChartView chartView;
        if (tags != null) {
            chartView = chartService.getStackedValueChart(tags, period);
        } else {
            chartView = chartService.lineChartFor(period, new ArrayList<>(all));
        }

        model.addAttribute("days", chartView.getTimes());
        model.addAttribute("dataset", chartView.getChartDatasets());
        return "index";
    }
}
