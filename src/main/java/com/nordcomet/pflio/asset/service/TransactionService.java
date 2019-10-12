package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.TransactionResponse;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Fee;
import com.nordcomet.pflio.asset.model.Transaction;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import com.nordcomet.pflio.asset.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalDateTime.now;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AssetPositionService assetPositionService;

    @Autowired
    private AssetRepo assetRepo;

    public void save(TransactionSaveRequest dto) {
        Asset asset = assetRepo.findAssetsById(dto.getAssetId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Fee fee = resolveFee(dto, asset);
        Transaction transaction = toTransaction(dto, asset, fee);
        assetPositionService.createBasedOn(transaction);
        transactionRepo.save(transaction);
    }

    private Fee resolveFee(TransactionSaveRequest dto, Asset asset) {
        BigDecimal feeAmount = calculateFeeAmount(dto);
        return new Fee(feeAmount, dto.getCurrency(), asset, now());
    }

    private BigDecimal calculateFeeAmount(TransactionSaveRequest dto) {
        return dto.getTotalPrice().subtract(
                dto.getUnitPrice().multiply(dto.getQuantityChange()))
                .setScale(4, RoundingMode.HALF_UP);
    }

    private Transaction toTransaction(TransactionSaveRequest dto, Asset asset, Fee fee) {
        return new Transaction(asset, dto.getTimestamp(), dto.getUnitPrice(), dto.getQuantityChange(), dto.getCurrency(), fee);
    }

    @Transactional
    public List<TransactionResponse> getTransactions() {
        return transactionRepo.findTop10ByOrderByTimestampDesc().stream()
                .map(t -> new TransactionResponse(
                        t.getTimestamp(),
                        t.getAsset().getName(),
                        t.getAsset().getAccount().getName(),
                        toDisplay(t.getPrice().multiply(t.getQuantityChange())),
                        toDisplay(t.getFee().getAmount()),
                        t.getCurrency()
                )).collect(Collectors.toList());
    }

    private BigDecimal toDisplay(BigDecimal value) {
        return value.setScale(2, HALF_UP);
    }
}
