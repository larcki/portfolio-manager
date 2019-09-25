package com.nordcomet.pflio;

import com.nordcomet.pflio.model.Tags;
import com.nordcomet.pflio.service.ChartService;
import com.nordcomet.pflio.view.ChartData;
import com.nordcomet.pflio.view.ChartOptions;
import com.nordcomet.pflio.view.ChartView;
import com.nordcomet.pflio.view.ProperChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BackendController {

    @Autowired
    private ChartService chartService;

    @RequestMapping("/api/chart")
    public ProperChart chart() {
        List<Tags> tags = List.of(Tags.BOND, Tags.STOCK);
        ChartView chartView = chartService.getStackedValueChart(tags, 180);


        ChartData chartData = new ChartData(chartView.getTimes(), chartView.getChartDatasets());
        Map<Object, Object> options = Map.of(
                "responsive", true,
                "title", Map.of(
                        "display", true,
                        "text", "Asset allocation by time"),
                "scales", Map.of(
                        "xAxes", List.of(Map.of(
                                "scaleLabel", Map.of(
                                        "display", true,
                                        "labelString", "Time"))),
                        "yAxes", List.of(Map.of(
                                "stacked", true,
                                "scaleLabel", Map.of(
                                        "display", true,
                                        "labelString", "value")))

                )
        );
        ProperChart properChart = new ProperChart("line", chartData, new ChartOptions(options));

        return properChart;
    }


}
