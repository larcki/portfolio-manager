package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    public void save(Transaction transaction) {
        checkThatIsLatest(transaction);
        BigDecimal previousQuantity = assetPositionService.resolveTotalQuantityForAsset(transaction.getAsset().getId());
        transactionRepo.save(transaction);
        assetPositionService.save(createAssetPosition(transaction, previousQuantity));
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

}
