package com.nordcomet.pflio.chart.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartJSDataset {
    private String label;
    private List<BigDecimal> data;
    private String backgroundColor;
    private String borderColor;
    private Boolean fill;

    public ChartJSDataset(String label, List<BigDecimal> data, String backgroundColor, String borderColor, Boolean fill) {
        this.label = label;
        this.data = data;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.fill = fill;
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

    public Boolean isFill() {
        return fill;
    }

    public String getBorderColor() {
        return borderColor;
    }
}
