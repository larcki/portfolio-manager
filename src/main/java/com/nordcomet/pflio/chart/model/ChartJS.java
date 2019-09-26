package com.nordcomet.pflio.chart.model;

public class ChartJS {

    private String type;
    private ChartJSData data;
    private ChartJSOptions options;

    public ChartJS() {
    }

    public ChartJS(String type, ChartJSData data, ChartJSOptions options) {
        this.type = type;
        this.data = data;
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public ChartJSData getData() {
        return data;
    }

    public ChartJSOptions getOptions() {
        return options;
    }
}
