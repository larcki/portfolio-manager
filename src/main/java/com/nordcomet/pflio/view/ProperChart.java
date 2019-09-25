package com.nordcomet.pflio.view;

public class ProperChart {

    private String type;
    private ChartData data;
    private ChartOptions options;

    public ProperChart() {
    }

    public ProperChart(String type, ChartData data, ChartOptions options) {
        this.type = type;
        this.data = data;
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public ChartData getData() {
        return data;
    }

    public ChartOptions getOptions() {
        return options;
    }
}
