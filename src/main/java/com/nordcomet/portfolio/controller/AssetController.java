package com.nordcomet.portfolio.controller;

import com.nordcomet.portfolio.service.asset.AssetDto;
import com.nordcomet.portfolio.service.asset.AssetInfoDto;
import com.nordcomet.portfolio.service.asset.AssetSelectionDto;
import com.nordcomet.portfolio.service.asset.AssetService;
import com.nordcomet.portfolio.service.assetposition.AssetPositionService;
import com.nordcomet.portfolio.service.assetposition.PortfolioSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api")
public class AssetController {

    private final AssetService assetService;
    private final AssetPositionService assetPositionService;

    @Autowired
    public AssetController(AssetService assetService, AssetPositionService assetPositionService) {
        this.assetService = assetService;
        this.assetPositionService = assetPositionService;
    }

    @RequestMapping("/assets")
    public List<AssetDto> getAssets() {
        return assetService.getAssetList();
    }

    @RequestMapping("/assets/{assetId}")
    public AssetInfoDto getAssets(@PathVariable("assetId") Integer assetId) {
        return assetService.getInfo(assetId);
    }

    @RequestMapping("/assets-min")
    public List<AssetSelectionDto> getAssetsForSelection() {
        return assetService.getAssetForSelection();
    }

    @RequestMapping("/summary")
    public PortfolioSummary getSummary() {
        return assetPositionService.getPortfolioSummary();
    }

}
