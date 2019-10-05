package com.nordcomet.pflio.chart.service;

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
            "#c26493",
            "#7bb949",
            "#a361c7",
            "#d5a142",
            "#6587cd",
            "#c8613f",
            "#52c0a7",
            "#d24066",
            "#4c8a4e",
            "#8c803a"
    );

    private static final List<String> colourScheme_30 = List.of(
            "#4fbf4e",
            "#924fc6",
            "#97bb38",
            "#526ad7",
            "#d2a636",
            "#da6ed9",
            "#498a34",
            "#c73d97",
            "#66bf7b",
            "#df3c73",
            "#51c3a8",
            "#d74639",
            "#3bb7cb",
            "#db7631",
            "#a184e4",
            "#8c8c2a",
            "#8c509a",
            "#a6b365",
            "#576aac",
            "#d9a16a",
            "#669ddb",
            "#a55331",
            "#36815b",
            "#e377a9",
            "#5f6d2c",
            "#c78fcc",
            "#8f6b2c",
            "#9d4867",
            "#e1817e",
            "#b93d4d"
    );

    public static Map<Object, String> createColourPalette(List<?> assets) {
        if (assets.size() <= 5) {
            return createPalette(colourScheme_5, assets);
        } else if (assets.size() <= 10) {
            return createPalette(colourScheme_10, assets);
        } else {
            return createPalette(colourScheme_30, assets);
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
