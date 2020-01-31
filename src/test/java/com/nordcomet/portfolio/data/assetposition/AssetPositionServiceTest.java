package com.nordcomet.portfolio.data.assetposition;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.transaction.Transaction;
import com.nordcomet.portfolio.service.assetposition.AssetPositionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AssetPositionServiceTest {

    private final AssetPositionRepo assetPositionRepo = mock(AssetPositionRepo.class);
    private final AssetPositionService underTest = new AssetPositionService(assetPositionRepo);

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