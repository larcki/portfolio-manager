package com.nordcomet.pflio.view;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.List;

public class ChartView {

    private List<LocalDate> times;
    private List<ChartDataset> chartDatasets;

    public ChartView(List<LocalDate> times, List<ChartDataset> chartDatasets) {
        this.times = times;
        this.chartDatasets = chartDatasets;
    }

    public List<LocalDate> getTimes() {
        return times;
    }

    public List<ChartDataset> getChartDatasets() {
        return chartDatasets;
    }
}
