package com.nordcomet.pflio;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tags;
import com.nordcomet.pflio.repo.AssetRepo;
import com.nordcomet.pflio.service.ChartService;
import com.nordcomet.pflio.view.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.StreamSupport;

@Controller
public class PortfolioController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private AssetRepo assetRepo;

    @RequestMapping("/")
    public String landing(Model model) {

        List<Tags> tags = List.of(Tags.BOND, Tags.STOCK);

        Iterable<Asset> all = assetRepo.findAssetsByTagsNameIn(tags);

        ChartView chartView = chartService.lineChartFor(180, StreamSupport.stream(all.spliterator(), false).toArray(Asset[]::new));

        model.addAttribute("days", chartView.getTimes());
        model.addAttribute("dataset", chartView.getChartDatasets());
        return "index";
    }
}
