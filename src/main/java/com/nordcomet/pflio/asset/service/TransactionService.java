package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Fee;
import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.TransactionDto;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Fee fee = resolveFee(dto, asset);
        Transaction transaction = toTransaction(dto, asset, fee);
        checkThatIsLatest(transaction);
        assetPositionService.createBasedOn(transaction);
        transactionRepo.save(transaction);
    }

    private Fee resolveFee(TransactionDto dto, Asset asset) {
        BigDecimal feeAmount = calculateFeeAmount(dto);
        return new Fee(feeAmount, dto.getCurrency(), asset, now());
    }

    private BigDecimal calculateFeeAmount(TransactionDto dto) {
        return dto.getTotalPrice().subtract(
                dto.getUnitPrice().multiply(dto.getQuantityChange()))
                .setScale(4, RoundingMode.HALF_UP);
    }

    private Transaction toTransaction(TransactionDto dto, Asset asset, Fee fee) {
        return new Transaction(asset, now(), dto.getUnitPrice(), dto.getQuantityChange(), dto.getCurrency(), fee);
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
