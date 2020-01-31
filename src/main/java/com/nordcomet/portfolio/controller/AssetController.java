package com.nordcomet.portfolio.controller;

import com.nordcomet.portfolio.controller.dto.PortfolioSummary;
import com.nordcomet.portfolio.service.asset.AssetDto;
import com.nordcomet.portfolio.service.asset.AssetInfoDto;
import com.nordcomet.portfolio.service.asset.AssetSelectionDto;
import com.nordcomet.portfolio.service.asset.AssetService;
import com.nordcomet.portfolio.service.assetposition.Profit;
import com.nordcomet.portfolio.service.assetposition.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class AssetController {

    private final AssetService assetService;
    private final ProfitService profitService;

    @Autowired
    public AssetController(AssetService assetService, ProfitService profitService) {
        this.assetService = assetService;
        this.profitService = profitService;
    }

    @RequestMapping("/api/assets")
    public List<AssetDto> getAssets() {
        return assetService.getAssetList();
    }

    @RequestMapping("/api/assets/{assetId}")
    public AssetInfoDto getAssets(@PathVariable("assetId") Integer assetId) {
        return assetService.getInfo(assetId);
    }

    @RequestMapping("/api/assets-min")
    public List<AssetSelectionDto> getAssetsForSelection() {
        return assetService.getAssetForSelection();
    }

    @RequestMapping("/api/summary")
    public PortfolioSummary getSummary() {
        Profit profit = profitService.getPortfolioProfit();
        return new PortfolioSummary(
                display(profit.getValue()),
                display(profit.getPurchaseAmount()),
                display(profit.getProfit()),
                display(profit.getProfitPercentage().multiply(new BigDecimal("100")))
        );
    }

    private String display(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }

}
