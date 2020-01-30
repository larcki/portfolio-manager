package com.nordcomet.portfolio.chart.chartjs;

import java.math.BigDecimal;
import java.util.List;

public class ChartJSDatasetBuilder {
    private String label;
    private String backgroundColor;
    private List<BigDecimal> data;
    private Boolean fill;
    private String color;

    public ChartJSDatasetBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public ChartJSDatasetBuilder setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ChartJSDatasetBuilder setData(List<BigDecimal> data) {
        this.data = data;
        return this;
    }

    public ChartJSDatasetBuilder setFill(Boolean fill) {
        this.fill = fill;
        return this;
    }

    public ChartJSDatasetBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public ChartJSDataset build() {
        return new ChartJSDataset(label, data, backgroundColor, color, fill);
    }
}