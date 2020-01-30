package com.nordcomet.portfolio.chart.chartjs;

import java.time.LocalDate;
import java.util.List;

public class ChartJSData {

    private List<LocalDate> labels;
    private List<ChartJSDataset> datasets;

    public ChartJSData(List<LocalDate> labels, List<ChartJSDataset> chartDataset) {
        this.labels = labels;
        this.datasets = chartDataset;
    }

    public ChartJSData() {
    }

    public List<LocalDate> getLabels() {
        return labels;
    }

    public List<ChartJSDataset> getDatasets() {
        return datasets;
    }
}
