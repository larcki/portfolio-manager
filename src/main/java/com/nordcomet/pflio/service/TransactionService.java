package com.nordcomet.pflio.service;

import com.nordcomet.pflio.model.Transaction;
import com.nordcomet.pflio.model.snapshot.AssetPosition;
import com.nordcomet.pflio.repo.AssetPositionRepo;
import com.nordcomet.pflio.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AssetPositionRepo assetPositionRepo;

    public void save(Transaction transaction) {
        checkThatIsLatest(transaction);
        transactionRepo.save(transaction);
        BigDecimal previousQuantity = resolvePreviousQuantity(transaction.getAsset().getId());
        assetPositionRepo.save(createAssetPosition(transaction, previousQuantity));
    }

    private AssetPosition createAssetPosition(Transaction transaction, BigDecimal previousQuantity) {
        BigDecimal newQuantity = previousQuantity.add(transaction.getQuantityChange());
        BigDecimal newTotalPrice = newQuantity.multiply(transaction.getPrice());
        return new AssetPosition(transaction.getAsset(), newQuantity, transaction.getPrice(), newTotalPrice, transaction.getTimestamp());
    }

    private void checkThatIsLatest(Transaction transaction) {
        Optional<Transaction> latestTransaction = transactionRepo.findFirstByAssetIdOrderByTimestampDesc(transaction.getAsset().getId());
        if (latestTransaction.isPresent()) {
            if (latestTransaction.get().getTimestamp().isAfter(transaction.getTimestamp())) {
                throw new IllegalArgumentException("Can not store transaction when more recent exists");
            }
        }
    }

    private BigDecimal resolvePreviousQuantity(Integer assetId) {
        Optional<AssetPosition> previousAssetPosition = assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(assetId);
        if (previousAssetPosition.isPresent()) {
            return previousAssetPosition.get().getQuantity();
        } else {
            return calculateQuantityFromPreviousTransactions();
        }
    }

    private BigDecimal calculateQuantityFromPreviousTransactions() {
        BigDecimal previousQuantity = BigDecimal.ZERO;
        for (Transaction previousTransaction : transactionRepo.findAll()) {
            previousQuantity = previousQuantity.add(previousTransaction.getQuantityChange());
        }
        return previousQuantity;
    }

}
