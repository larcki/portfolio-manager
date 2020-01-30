package com.nordcomet.portfolio.chart.chartjs;

import java.util.Map;

public class ChartJS {

    private String type;
    private ChartJSData data;
    private Map<Object, Object> options;

    public ChartJS() {
    }

    public ChartJS(String type, ChartJSData data, Map<Object, Object> options) {
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

    public Map<Object, Object> getOptions() {
        return options;
    }
}
