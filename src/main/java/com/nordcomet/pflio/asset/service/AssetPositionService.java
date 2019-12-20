package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@Slf4j
public class AssetPositionService {

    private final AssetPositionRepo assetPositionRepo;
    private final AssetRepo assetRepo;

    @Autowired
    public AssetPositionService(AssetPositionRepo assetPositionRepo, AssetRepo assetRepo) {
        this.assetPositionRepo = assetPositionRepo;
        this.assetRepo = assetRepo;
    }

    public AssetPosition createBasedOn(Transaction transaction) {
        AssetPosition latestAssetPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(transaction.getAsset().getId())
                .orElse(new AssetPosition(transaction.getAsset(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDateTime.now()));

        BigDecimal unitPrice = unitPriceInBaseCurrency(transaction);
        BigDecimal newQuantity = latestAssetPosition.getQuantity().add(transaction.getQuantityChange());
        BigDecimal newTotalValue = unitPrice.multiply(newQuantity).setScale(12, RoundingMode.HALF_UP);
        BigDecimal purchaseAmount = unitPrice.multiply(transaction.getQuantityChange()).setScale(12, RoundingMode.HALF_UP);
        BigDecimal newTotalPurchaseAmount = latestAssetPosition.getTotalPurchaseAmount().add(purchaseAmount);

        AssetPosition assetPosition = new AssetPosition(transaction.getAsset(), newQuantity, unitPrice, newTotalValue, newTotalPurchaseAmount, transaction.getTimestamp());
        return save(assetPosition);
    }

    public AssetPosition save(AssetPosition assetPosition) {
        log.info("Saved asset position for {} - {}", assetPosition.getAsset().getName(), assetPosition);
        return assetPositionRepo.save(assetPosition);
    }

    public BigDecimal getTotalValueOf(Asset asset) {
        return assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())
                .map(AssetPosition::getTotalValue)
                .orElse(BigDecimal.ZERO);
    }

    public PortfolioSummary getPortfolioSummary() {
        Set<Asset> allAssets = assetRepo.findAll();
        List<AssetPosition> assetPositions = assetPositionRepo.findAllByAssetIdInOrderByTimestampDesc(allAssets.stream().map(Asset::getId).collect(Collectors.toList()));

        BigDecimal totalValue = allAssets.stream()
                .map(asset -> findLatestAssetPosition(asset, assetPositions))
                .map(AssetPosition::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPurchaseAmount = allAssets.stream()
                .map(asset -> findLatestAssetPosition(asset, assetPositions))
                .map(AssetPosition::getTotalPurchaseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfit = totalValue.subtract(totalPurchaseAmount);
        BigDecimal totalProfitPercentage = totalValue.divide(totalPurchaseAmount, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

        return new PortfolioSummary(
                display(totalValue),
                display(totalPurchaseAmount),
                display(totalProfit),
                display(totalProfitPercentage.multiply(new BigDecimal("100")))
        );
    }

    public List<AssetPosition> findAssetPositionsForAssetStartingFrom(Asset asset, LocalDate date) {
        Optional<AssetPosition> firstAssetPosition = assetPositionRepo.findFirstByAssetIdAndTimestampBeforeOrderByTimestampDesc(
                asset.getId(), date.atStartOfDay()
        );
        List<AssetPosition> assetPositions = assetPositionRepo.findAllByAssetIdAndTimestampAfter(
                asset.getId(), firstAssetPosition.map(AssetPosition::getTimestamp).orElse(tenDaysBefore(date))
        );
        List<AssetPosition> assetPositionsToInclude = new ArrayList<>();
        firstAssetPosition.ifPresent(assetPositionsToInclude::add);
        assetPositionsToInclude.addAll(assetPositions);
        return assetPositionsToInclude;
    }

    private LocalDateTime tenDaysBefore(LocalDate date) {
        return date.minusDays(10).atStartOfDay();
    }

    private BigDecimal unitPriceInBaseCurrency(Transaction transaction) {
        Currency baseCurrency = transaction.getAsset().getBaseCurrency();
        Money unitPrice = transaction.getUnitPrice();

        if (unitPrice.getCurrency() != baseCurrency) {
            return unitPrice.getAmount().multiply(transaction.getExchangeRate()).setScale(12, RoundingMode.HALF_UP);
        }

        return unitPrice.getAmount();
    }

    private AssetPosition findLatestAssetPosition(Asset asset, List<AssetPosition> assetPositions) {
        return assetPositions.stream().filter(assetPosition -> asset.getId().equals(assetPosition.getAsset().getId()))
                .max(comparing(AssetPosition::getTimestamp))
                .orElseThrow(IllegalAccessError::new);
    }

    private String display(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
