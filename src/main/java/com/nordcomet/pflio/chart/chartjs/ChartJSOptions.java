package com.nordcomet.pflio.chart.chartjs;

import java.util.Map;

public class ChartJSOptions {

    private Map<Object, Object> scales;

    public ChartJSOptions() {
    }

    public ChartJSOptions(Map<Object, Object> scales) {
        this.scales = scales;
    }

    public Map<Object, Object> getScales() {
        return scales;
    }
}
