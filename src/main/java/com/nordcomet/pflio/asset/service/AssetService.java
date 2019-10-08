package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetDto;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@Service
public class AssetService {

    private final AssetRepo assetRepo;
    private final AssetPositionRepo assetPositionRepo;

    @Autowired
    public AssetService(AssetRepo assetRepo, AssetPositionRepo assetPositionRepo) {
        this.assetRepo = assetRepo;
        this.assetPositionRepo = assetPositionRepo;
    }

    public List<AssetDto> getAssetList() {
        return assetRepo.findAll().stream()
                .map(asset -> assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())
                        .map(position -> calculateAssetDto(asset, position))
                        .orElse(assetDto(asset, ZERO, ZERO, ONE)))
                .collect(Collectors.toList());
    }

    private AssetDto calculateAssetDto(Asset asset, AssetPosition position) {
        BigDecimal currentValue = position.getTotalPrice();
        BigDecimal performance = position.getTotalPrice().subtract(position.getTotalPurchaseAmount());
        BigDecimal performancePercentage = position.getTotalPrice().divide(position.getTotalPurchaseAmount(), 4, HALF_UP);
        return assetDto(asset, currentValue, performance, performancePercentage);
    }

    private AssetDto assetDto(Asset asset, BigDecimal currentValue, BigDecimal performance, BigDecimal performancePercentage) {
        return new AssetDto(asset.getName(),
                "Account",
                toDisplay(currentValue),
                toDisplay(performance),
                toDisplayPercentage(performancePercentage),
                asset.getId()
        );
    }

    private String toDisplay(BigDecimal value) {
        return value.setScale(2, HALF_UP).toString();
    }

    private String toDisplayPercentage(BigDecimal value) {
        return value.subtract(ONE).multiply(new BigDecimal("100")).setScale(1, HALF_UP).toString();
    }

}
