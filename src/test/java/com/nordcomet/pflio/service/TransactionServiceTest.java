package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Transaction;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.repo.AssetRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.nordcomet.pflio.DataRandomiser.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void save_shouldCreateAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        Transaction transaction = randomTransaction(asset);
        underTest.save(transaction);

        List<AssetPosition> positions = assetPositionRepo.findAllByAssetId(asset.getId());
        BigDecimal expectedTotalPrice = transaction.getQuantityChange().multiply(transaction.getPrice()).setScale(4, RoundingMode.HALF_UP);
        assertThat(positions.size(), is(1));
        AssetPosition assetPosition = positions.get(0);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getPrice()));
        assertThat(assetPosition.getQuantity(), is(transaction.getQuantityChange()));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldCreateAssetPositionWithQuantityCalculatedFromPreviousAssetPosition() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        AssetPosition previousPosition = randomAssetPosition(asset);
        assetPositionRepo.save(previousPosition);

        Transaction transaction = randomTransaction(asset);
        underTest.save(transaction);

        Optional<AssetPosition> latestPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(latestPosition.isPresent(), is(true));
        AssetPosition assetPosition = latestPosition.get();
        BigDecimal expectedTotalQuantity = previousPosition.getQuantity().add(transaction.getQuantityChange());
        BigDecimal expectedTotalPrice = expectedTotalQuantity.multiply(transaction.getPrice()).setScale(4, RoundingMode.HALF_UP);
        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(transaction.getPrice()));
        assertThat(assetPosition.getQuantity(), is(expectedTotalQuantity));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldCreateAssetPositionWithQuantityCalculatedFromPreviousTransactions_whenNoPreviousAssetPositions() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        Transaction transaction_1 = randomTransaction(asset);
        transaction_1.setTimestamp(LocalDateTime.now().minusDays(2));
        underTest.save(transaction_1);

        Transaction transaction_2 = randomTransaction(asset);
        transaction_2.setTimestamp(LocalDateTime.now().minusDays(1));
        underTest.save(transaction_2);

        Transaction latestTransaction = randomTransaction(asset);
        underTest.save(latestTransaction);

        Optional<AssetPosition> latestPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId());
        assertThat(latestPosition.isPresent(), is(true));
        AssetPosition assetPosition = latestPosition.get();

        BigDecimal expectedTotalQuantity = transaction_1.getQuantityChange()
                .add(transaction_2.getQuantityChange())
                .add(latestTransaction.getQuantityChange());
        BigDecimal expectedTotalPrice = expectedTotalQuantity
                .multiply(latestTransaction.getPrice())
                .setScale(4, RoundingMode.HALF_UP);

        assertThat(assetPosition.getAsset().getId(), is(asset.getId()));
        assertThat(assetPosition.getPrice(), is(latestTransaction.getPrice()));
        assertThat(assetPosition.getQuantity(), is(expectedTotalQuantity));
        assertThat(assetPosition.getTotalPrice(), is(expectedTotalPrice));
    }

    @Test
    void save_shouldThrowException_whenNewerTransactionExists() {
        Asset asset = randomAsset();
        assetRepo.save(asset);
        Transaction transaction_1 = randomTransaction(asset);
        transaction_1.setTimestamp(LocalDateTime.now());
        underTest.save(transaction_1);

        Transaction transaction_2 = randomTransaction(asset);
        transaction_2.setTimestamp(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> underTest.save(transaction_2));
    }
}