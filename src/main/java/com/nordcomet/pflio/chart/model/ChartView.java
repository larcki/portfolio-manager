package com.nordcomet.pflio.chart.model;

import com.nordcomet.pflio.chart.chartjs.ChartJSDataset;

import java.time.LocalDate;
import java.util.List;

public class ChartView {

    private List<LocalDate> times;
    private List<ChartJSDataset> chartDatasets;

    public ChartView(List<LocalDate> times, List<ChartJSDataset> chartDatasets) {
        this.times = times;
        this.chartDatasets = chartDatasets;
    }

    public List<LocalDate> getTimes() {
        return times;
    }

    public List<ChartJSDataset> getChartDatasets() {
        return chartDatasets;
    }
}
