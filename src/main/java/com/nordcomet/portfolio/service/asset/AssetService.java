package com.nordcomet.portfolio.service.asset;

import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
        BigDecimal currentValue = position.getTotalValue();
        BigDecimal performance = position.getTotalValue().subtract(position.getTotalPurchaseAmount());
        BigDecimal performancePercentage = position.getTotalValue().divide(position.getTotalPurchaseAmount(), 4, HALF_UP);
        return assetDto(asset, currentValue, performance, performancePercentage);
    }

    private AssetDto assetDto(Asset asset, BigDecimal currentValue, BigDecimal performance, BigDecimal performancePercentage) {
        return AssetDto.builder()
                .name(asset.getName())
                .account(asset.getAccount().getName())
                .totalValue(toDisplay(currentValue))
                .performance(toDisplay(performance))
                .performancePercentage(toDisplayPercentage(performancePercentage))
                .id(asset.getId())
                .build();
    }

    private String toDisplay(BigDecimal value) {
        return value.setScale(2, HALF_UP).toString();
    }

    private String toDisplayPercentage(BigDecimal value) {
        return value.subtract(ONE).multiply(new BigDecimal("100")).setScale(1, HALF_UP).toString();
    }

    public AssetInfoDto getInfo(Integer assetId) {
        Asset asset = assetRepo.findAssetsById(assetId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        BigDecimal value = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())
                .map(AssetPosition::getTotalValue)
                .orElse(BigDecimal.ZERO);

        return new AssetInfoDto(asset.getName(), "https://www.google.com", asset.getAccount().getName(), value);

    }

    public List<AssetSelectionDto> getAssetForSelection() {
        return assetRepo.findAll().stream()
                .map(asset -> AssetSelectionDto.builder()
                        .id(asset.getId())
                        .name(asset.getName())
                        .account(asset.getAccount().getName())
                        .currency(asset.getAccount().getDefaultCurrency())
                        .build())
                .collect(Collectors.toList());
    }
}
