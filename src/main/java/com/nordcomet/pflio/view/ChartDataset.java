package com.nordcomet.pflio.view;

import java.math.BigDecimal;
import java.util.List;

public class ChartDataset {
    private String label;
    private List<BigDecimal> data;
    private String colour;

    public ChartDataset(String label, String colour, List<BigDecimal> data) {
        this.label = label;
        this.colour = colour;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public String getColour() {
        return colour;
    }

    public List<BigDecimal> getData() {
        return data;
    }
}
