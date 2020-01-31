package com.nordcomet.portfolio.service.assetposition;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import com.nordcomet.portfolio.data.transaction.Transaction;
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

import static com.nordcomet.portfolio.service.assetposition.AssetPositionFunctions.beforeEndOfDay;
import static java.util.stream.Collectors.toList;


@Service
@Slf4j
public class AssetPositionService {

    private final AssetPositionRepo assetPositionRepo;

    @Autowired
    public AssetPositionService(AssetPositionRepo assetPositionRepo) {
        this.assetPositionRepo = assetPositionRepo;
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

    public List<AssetPosition> findAssetPositionsByDateFor(Set<Asset> allAssets, LocalDateTime localDateTime) {
        return assetPositionRepo.findAllByAssetIdInOrderByTimestampDesc(allAssets.stream()
                .map(Asset::getId)
                .collect(toList())).stream()
                .filter(beforeEndOfDay(localDateTime))
                .collect(toList());
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

}
