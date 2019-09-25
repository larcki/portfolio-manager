package com.nordcomet.pflio.view;

import java.util.Map;

public class ChartOptions {

    private Map<Object, Object> scales;

    public ChartOptions() {
    }

    public ChartOptions(Map<Object, Object> scales) {
        this.scales = scales;
    }

    public Map<Object, Object> getScales() {
        return scales;
    }
}
