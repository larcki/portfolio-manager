package com.nordcomet.portfolio.service.assetposition;

import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparing;

@Service
public class ProfitService {

    private final AssetRepo assetRepo;
    private final AssetPositionService assetPositionService;

    @Autowired
    public ProfitService(AssetRepo assetRepo, AssetPositionService assetPositionService) {
        this.assetRepo = assetRepo;
        this.assetPositionService = assetPositionService;
    }

    public Profit getPortfolioProfit() {
        return getProfitUntil(now());
    }

    public Profit getPortfolioProfitSince(LocalDateTime localDateTime) {
        Profit profitAtDate = getProfitUntil(localDateTime);
        Profit profitNow = getProfitUntil(now());

        BigDecimal value = profitNow.getValue().subtract(profitAtDate.getValue());
        BigDecimal purchaseAmount = profitNow.getPurchaseAmount().subtract(profitAtDate.getPurchaseAmount());
        BigDecimal profit = profitNow.getProfit().subtract(profitAtDate.getProfit());
        BigDecimal profitPercentage = profitNow.getProfit().divide(profitAtDate.getProfit(), 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

        return Profit.builder()
                .value(value)
                .purchaseAmount(purchaseAmount)
                .profit(profit)
                .profitPercentage(profitPercentage)
                .build();
    }

    private Profit getProfitUntil(LocalDateTime localDateTime) {
        Set<Asset> allAssets = assetRepo.findAll();
        List<AssetPosition> assetPositions = assetPositionService.findAssetPositionsByDateFor(allAssets, localDateTime);

        BigDecimal value = calculateTotalValue(allAssets, assetPositions);
        BigDecimal purchaseAmount = calculateTotalPurchaseAmount(allAssets, assetPositions);
        BigDecimal profit = value.subtract(purchaseAmount);
        BigDecimal profitPercentage = value.divide(purchaseAmount, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

        return Profit.builder()
                .value(value)
                .purchaseAmount(purchaseAmount)
                .profit(profit)
                .profitPercentage(profitPercentage)
                .build();
    }

    private BigDecimal calculateTotalPurchaseAmount(Set<Asset> allAssets, List<AssetPosition> assetPositions) {
        return allAssets.stream()
                .map(asset -> findLatestAssetPosition(asset, assetPositions))
                .map(AssetPosition::getTotalPurchaseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalValue(Set<Asset> allAssets, List<AssetPosition> assetPositions) {
        return allAssets.stream()
                .map(asset -> findLatestAssetPosition(asset, assetPositions))
                .map(AssetPosition::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private AssetPosition findLatestAssetPosition(Asset asset, List<AssetPosition> assetPositions) {
        return assetPositions.stream().filter(assetPosition -> asset.getId().equals(assetPosition.getAsset().getId()))
                .max(comparing(AssetPosition::getTimestamp))
                .orElseThrow(IllegalAccessError::new);
    }

}
