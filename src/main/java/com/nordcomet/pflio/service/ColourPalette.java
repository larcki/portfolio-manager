package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPalette {

    private static final List<String> colourScheme_5 = List.of(
            "#638ccc",
            "#c57c3c",
            "#ab62c0",
            "#72a555",
            "#ca5670"
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

    public static Map<Integer, String> createColourPalette(List<Asset> assets) {
        if (assets.size() <= 5) {
            return createPalette(colourScheme_5, assets);
        } else if (assets.size() <= 10) {
            return createPalette(colourScheme_10, assets);
        } else {
            return createPalette(colourScheme_30, assets);
        }
    }

    private static Map<Integer, String> createPalette(List<String> colourScheme_10, List<Asset> assets) {
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < assets.size(); i++) {
            result.put(assets.get(i).getId(), colourScheme_10.get(i));
        }
        return result;
    }


}
