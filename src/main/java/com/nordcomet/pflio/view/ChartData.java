package com.nordcomet.pflio.view;

import java.time.LocalDate;
import java.util.List;

public class ChartData {

    private List<LocalDate> labels;

    private List<ChartDataset> datasets;

    public ChartData(List<LocalDate> labels, List<ChartDataset> chartDataset) {
        this.labels = labels;
        this.datasets = chartDataset;
    }

    public ChartData() {
    }

    public List<LocalDate> getLabels() {
        return labels;
    }

    public List<ChartDataset> getDatasets() {
        return datasets;
    }
}
