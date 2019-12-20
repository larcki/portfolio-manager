package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.repo.AssetPositionRepo;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AssetPositionServiceTest {

    private final AssetPositionRepo assetPositionRepo = mock(AssetPositionRepo.class);
    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final AssetPositionService underTest = new AssetPositionService(assetPositionRepo, assetRepo);

    @Test
    void shouldSaveAssetPosition() {
        Transaction transaction = Transaction.builder()
                .quantityChange(new BigDecimal("5.5449"))
                .unitPrice(Money.of(new BigDecimal("138.2495627"), Currency.NOK))
                .exchangeRate(new BigDecimal("0.104358894366326"))
                .totalAmount(Money.of(new BigDecimal("79.999441265799"), Currency.EUR))
                .asset(Asset.builder().baseCurrency(Currency.EUR).build())
                .timestamp(LocalDateTime.now())
                .build();

        underTest.createBasedOn(transaction);

        verify(assetPositionRepo).save(new AssetPosition(
                transaction.getAsset(),
                transaction.getQuantityChange(),
                new BigDecimal("14.427571510000"),
                transaction.getTotalAmount().getAmount(),
                transaction.getTotalAmount().getAmount(),
                transaction.getTimestamp()
        ));

    }
}