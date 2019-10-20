package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.TransactionResponse;
import com.nordcomet.pflio.asset.model.*;
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

        Fee fee = Fee.builder()
                .amount(new Money(calculateFeeAmount(dto), dto.getTotalAmount().getCurrency()))
                .asset(asset)
                .timestamp(dto.getTimestamp())
                .build();

        Transaction transaction = Transaction.builder()
                .asset(asset)
                .unitPrice(dto.getUnitPrice())
                .quantityChange(dto.getQuantityChange())
                .fee(fee)
                .totalAmount(dto.getTotalAmount())
                .timestamp(dto.getTimestamp())
                .build();

        assetPositionService.createBasedOn(transaction);
        transactionRepo.save(transaction);
    }

    private BigDecimal calculateFeeAmount(TransactionSaveRequest dto) {
        return dto.getTotalAmount().getAmount().subtract(
                dto.getUnitPrice().getAmount().multiply(dto.getQuantityChange()))
                .setScale(4, RoundingMode.HALF_UP);
    }

    @Transactional
    public List<TransactionResponse> getTransactions() {
        return transactionRepo.findTop10ByOrderByTimestampDesc().stream()
                .map(t -> new TransactionResponse(
                        t.getTimestamp(),
                        t.getAsset().getName(),
                        t.getAsset().getAccount().getName(),
                        toDisplay(t.getUnitPrice().getAmount().multiply(t.getQuantityChange())),
                        toDisplay(t.getFee().getAmount().getAmount()),
                        t.getTotalAmount().getCurrency().toString()
                )).collect(Collectors.toList());
    }

    private BigDecimal toDisplay(BigDecimal value) {
        return value.setScale(2, HALF_UP);
    }
}
