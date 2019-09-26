package com.nordcomet.pflio.chart.model;

import java.math.BigDecimal;
import java.util.List;

public class ChartJSDataset {
    private String label;
    private List<BigDecimal> data;
    private String backgroundColor;

    public ChartJSDataset(String label, String backgroundColor, List<BigDecimal> data) {
        this.label = label;
        this.backgroundColor = backgroundColor;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public List<BigDecimal> getData() {
        return data;
    }
}
