package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.*;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.nordcomet.pflio.DataRandomiser.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionServiceTest {

    @Autowired
    private TransactionService underTest;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private FeeRepo feeRepo;

    @Test
    void save_shouldSaveTransactionWithFee() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        BigDecimal quantity = new BigDecimal("2");
        BigDecimal unitPrice = new BigDecimal("3");
        BigDecimal totalPrice = new BigDecimal("7");
        TransactionSaveRequest dto = TransactionSaveRequest.builder()
                .assetId(asset.getId())
                .quantityChange(quantity)
                .unitPrice(Money.of(unitPrice, Currency.GBP))
                .totalAmount(Money.of(totalPrice, Currency.GBP))
                .timestamp(LocalDateTime.now())
                .exchangeRate(BigDecimal.ONE)
                .build();

        underTest.save(dto);

        List<Transaction> transaction = transactionRepo.findAllByAssetId(asset.getId());
        assertEquals(transaction.size(), 1);
        assertEquals(transaction.get(0).getQuantityChange(), new BigDecimal("2.0000"));
        assertEquals(transaction.get(0).getUnitPrice().getAmount(), new BigDecimal("3.0000"));
        assertEquals(transaction.get(0).getFee().getAmount().getAmount(), new BigDecimal("1.0000"));
        assertEquals(transaction.get(0).getTotalAmount().getCurrency(), Currency.GBP);

        Optional<Fee> fee = feeRepo.findFeeByAssetId(asset.getId());
        assertTrue(fee.isPresent());
        assertEquals(fee.get().getAmount().getAmount(), new BigDecimal("1.0000"));
        assertEquals(fee.get().getAmount().getCurrency(), Currency.GBP);
    }

    @Test
    void save_shouldThrowException_whenAssetNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            TransactionSaveRequest request = TransactionSaveRequest.builder().build();
            underTest.save(request);
        });
    }

    @Test
    void save_shouldCreateAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        TransactionSaveRequest transaction = randomTransactionDto(asset);
        underTest.save(transaction);

        List<AssetPosition> positions = assetPositionRepo.findAllByAssetId(asset.getId());
        BigDecimal expectedTotalPrice = transaction.getQuantityChange().multiply(transaction.getUnitPrice().getAmount()).setScale(4, RoundingMode.HALF_UP);
        assertThat(positions.size(), is(1));
        AssetPosition assetPosition = positions.get(0);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getUnitPrice().getAmount()));
        assertThat(assetPosition.getQuantity(), is(transaction.getQuantityChange()));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
        assertThat(assetPosition.getTotalPurchaseAmount(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldCreateAssetPositionWithQuantityCalculatedFromPreviousAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        AssetPosition previousPosition = randomAssetPosition(asset);
        assetPositionRepo.save(previousPosition);

        TransactionSaveRequest transaction = randomTransactionDto(asset);
        underTest.save(transaction);

        Optional<AssetPosition> latestPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(latestPosition.isPresent(), is(true));
        AssetPosition assetPosition = latestPosition.get();
        BigDecimal expectedTotalQuantity = previousPosition.getQuantity().add(transaction.getQuantityChange());
        BigDecimal expectedTotalPrice = expectedTotalQuantity.multiply(transaction.getUnitPrice().getAmount()).setScale(4, RoundingMode.HALF_UP);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getUnitPrice().getAmount()));
        assertThat(assetPosition.getQuantity(), is(expectedTotalQuantity));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
        BigDecimal expectedTotalPurchaseAmount = previousPosition.getTotalPurchaseAmount().add(transaction.getUnitPrice().getAmount().multiply(transaction.getQuantityChange())).setScale(4, RoundingMode.HALF_UP);
        assertThat(assetPosition.getTotalPurchaseAmount(), is(expectedTotalPurchaseAmount));
    }

}