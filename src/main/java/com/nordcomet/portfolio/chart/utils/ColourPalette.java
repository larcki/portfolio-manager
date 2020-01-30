package com.nordcomet.portfolio.chart.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPalette {

    private static final List<String> colourScheme_5 = List.of(
            "rgb(212,221,185)",
            "rgb(165,201,227)",
            "rgb(218,187,214)",
            "rgb(222,182,163)",
            "rgb(164,209,197)"
    );

    private static final List<String> colourScheme_10 = List.of(
            "rgb(148,199,177)",
            "rgb(214,190,226)",
            "rgb(207,236,206)",
            "rgb(136,174,225)",
            "rgb(216,199,166)",
            "rgb(149,187,239)",
            "rgb(231,184,183)",
            "rgb(152,212,228)",
            "rgb(172,194,180)",
            "rgb(180,191,218)"
    );

    public static String getOne() {
        return colourScheme_5.get(0);
    }

    public static String getSecond() {
        return colourScheme_5.get(1);
    }

    public static String getTransparent() {
        return "rgba(220,220,220,0)";
    }

    public static Map<Object, String> createColourPalette(List<?> assets) {
        if (assets.size() <= 5) {
            return createPalette(colourScheme_5, assets);
        } else {
            return createPalette(colourScheme_10, assets);
        }
    }

    private static Map<Object, String> createPalette(List<String> colourScheme_10, List<?> assets) {
        Map<Object, String> result = new HashMap<>();
        for (int i = 0; i < assets.size(); i++) {
            result.put(assets.get(i), colourScheme_10.get(i));
        }
        return result;
    }


}
