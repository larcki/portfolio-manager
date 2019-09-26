package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.Tags;
import com.nordcomet.pflio.chart.model.ChartJS;
import com.nordcomet.pflio.chart.model.ChartJSData;
import com.nordcomet.pflio.chart.model.PortfolioChartType;
import com.nordcomet.pflio.chart.service.ChartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.nordcomet.pflio.chart.service.ChartJSFactory.createChartJS;

@RestController
public class ChartController {

    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);

    @Autowired
    private ChartService chartService;

    @RequestMapping("/api/chart")
    public ChartJS chart(@RequestParam Integer period,
                         @RequestParam PortfolioChartType chartType,
                         @RequestParam(required = false) List<Tags> tags,
                         @RequestParam(required = false) List<Integer> assets) {

        logger.info("Incoming request, period {} chartType {} tags {} assets {}", period, chartType, tags, assets);

        if (tags == null && assets == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (chartType.equals(PortfolioChartType.STACKED_VALUE)) {
            ChartJSData chartData = chartService.getStackedValueChart(tags, period);
            return createChartJS(
                    chartType,
                    "Allocation over time",
                    chartData);
        }

        throw new RuntimeException("Chart type not implemented");


    }


}
