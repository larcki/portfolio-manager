package com.nordcomet.pflio.chart;

import com.nordcomet.pflio.asset.model.Tags;
import com.nordcomet.pflio.chart.chartjs.ChartJSData;
import com.nordcomet.pflio.chart.chartjs.ChartJSOptions;
import com.nordcomet.pflio.chart.chartjs.ChartJS;
import com.nordcomet.pflio.chart.model.ChartView;
import com.nordcomet.pflio.chart.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @RequestMapping("/api/chart")
    public ChartJS chart() {
        List<Tags> tags = List.of(Tags.BOND, Tags.STOCK);
        ChartView chartView = chartService.getStackedValueChart(tags, 180);


        ChartJSData chartData = new ChartJSData(chartView.getTimes(), chartView.getChartDatasets());
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
        ChartJS properChart = new ChartJS("line", chartData, new ChartJSOptions(options));

        return properChart;
    }


}
