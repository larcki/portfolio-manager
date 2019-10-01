package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Fee;
import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.TransactionDto;
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
        TransactionDto dto = new TransactionDto(asset.getId(), quantity, unitPrice, totalPrice, "GBP", LocalDateTime.now());

        underTest.save(dto);

        List<Transaction> transaction = transactionRepo.findAllByAssetId(asset.getId());
        assertEquals(transaction.size(), 1);
        assertEquals(transaction.get(0).getQuantityChange(), new BigDecimal("2.0000"));
        assertEquals(transaction.get(0).getPrice(), new BigDecimal("3.0000"));
        assertEquals(transaction.get(0).getFee().getAmount(), new BigDecimal("1.0000"));
        assertEquals(transaction.get(0).getCurrency(), "GBP");

        Optional<Fee> fee = feeRepo.findFeeByAssetId(asset.getId());
        assertTrue(fee.isPresent());
        assertEquals(fee.get().getAmount(), new BigDecimal("1.0000"));
        assertEquals(fee.get().getCurrency(), "GBP");
    }

    @Test
    void save_shouldThrowException_whenAssetNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            underTest.save(new TransactionDto(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "EUR", LocalDateTime.now()));
        });
    }

    @Test
    void save_shouldCreateAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        TransactionDto transaction = randomTransactionDto(asset);
        underTest.save(transaction);

        List<AssetPosition> positions = assetPositionRepo.findAllByAssetId(asset.getId());
        BigDecimal expectedTotalPrice = transaction.getQuantityChange().multiply(transaction.getUnitPrice()).setScale(4, RoundingMode.HALF_UP);
        assertThat(positions.size(), is(1));
        AssetPosition assetPosition = positions.get(0);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getUnitPrice()));
        assertThat(assetPosition.getQuantity(), is(transaction.getQuantityChange()));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldCreateAssetPositionWithQuantityCalculatedFromPreviousAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        AssetPosition previousPosition = randomAssetPosition(asset);
        assetPositionRepo.save(previousPosition);

        TransactionDto transaction = randomTransactionDto(asset);
        underTest.save(transaction);

        Optional<AssetPosition> latestPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(latestPosition.isPresent(), is(true));
        AssetPosition assetPosition = latestPosition.get();
        BigDecimal expectedTotalQuantity = previousPosition.getQuantity().add(transaction.getQuantityChange());
        BigDecimal expectedTotalPrice = expectedTotalQuantity.multiply(transaction.getUnitPrice()).setScale(4, RoundingMode.HALF_UP);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getUnitPrice()));
        assertThat(assetPosition.getQuantity(), is(expectedTotalQuantity));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldCreateAssetPositionWithQuantityCalculatedFromPreviousTransactions_whenNoPreviousAssetPositions() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        Transaction transaction_1 = randomTransaction(asset);
        transaction_1.setTimestamp(LocalDateTime.now().minusDays(2));
        transactionRepo.save(transaction_1);

        Transaction transaction_2 = randomTransaction(asset);
        transaction_2.setTimestamp(LocalDateTime.now().minusDays(1));
        transactionRepo.save(transaction_2);

        TransactionDto latestTransaction = randomTransactionDto(asset);
        underTest.save(latestTransaction);

        Optional<AssetPosition> latestPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(latestPosition.isPresent(), is(true));
        AssetPosition assetPosition = latestPosition.get();

        BigDecimal expectedTotalQuantity = transaction_1.getQuantityChange()
                .add(transaction_2.getQuantityChange())
                .add(latestTransaction.getQuantityChange());
        BigDecimal expectedTotalPrice = expectedTotalQuantity
                .multiply(latestTransaction.getUnitPrice())
                .setScale(4, RoundingMode.HALF_UP);

        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(latestTransaction.getUnitPrice()));
        assertThat(assetPosition.getQuantity(), is(expectedTotalQuantity));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

}