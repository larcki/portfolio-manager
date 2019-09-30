package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.TransactionDto;
import com.nordcomet.pflio.asset.model.snapshot.AssetPosition;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    @Autowired
    private AssetRepo assetRepo;

    public void save(TransactionDto dto) {
        Asset asset = assetRepo.findAssetsById(dto.getAssetId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        BigDecimal fee = dto.getTotalPrice().subtract(
                dto.getUnitPrice().multiply(dto.getQuantity())
        );
        save(new Transaction(asset, now(), dto.getUnitPrice(), dto.getQuantity(), fee, dto.getCurrency()));
    }

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
