package com.nordcomet.portfolio.service.transaction;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.fee.Fee;
import com.nordcomet.portfolio.data.transaction.Transaction;
import com.nordcomet.portfolio.data.transaction.TransactionRepo;
import com.nordcomet.portfolio.service.assetposition.AssetPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final AssetPositionService assetPositionService;
    private final AssetRepo assetRepo;

    @Autowired
    public TransactionService(TransactionRepo transactionRepo, AssetPositionService assetPositionService, AssetRepo assetRepo) {
        this.transactionRepo = transactionRepo;
        this.assetPositionService = assetPositionService;
        this.assetRepo = assetRepo;
    }

    @Transactional
    public void save(TransactionSaveRequest dto) {
        log.info("Saving transaction for Asset {} date {}", dto.getAssetId(), dto.getTimestamp());

        Asset asset = assetRepo.findAssetsById(dto.getAssetId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Fee fee = Fee.builder()
                .amount(calculateFee(dto))
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
                .exchangeRate(dto.getExchangeRate())
                .build();

        checkThatIsLatest(transaction);

        assetPositionService.createBasedOn(transaction);
        transactionRepo.save(transaction);
    }

    private Money calculateFee(TransactionSaveRequest dto) {
        BigDecimal totalPrice = dto.getUnitPrice().getAmount()
                .multiply(dto.getQuantityChange())
                .multiply(dto.getExchangeRate());
        BigDecimal feeAmount = dto.getTotalAmount().getAmount().subtract(totalPrice);
        return Money.of(feeAmount, dto.getTotalAmount().getCurrency());
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

    private void checkThatIsLatest(Transaction transaction) {
        Optional<Transaction> latestTransaction = transactionRepo.findFirstByAssetIdOrderByTimestampDesc(transaction.getAsset().getId());
        if (latestTransaction.isPresent()) {
            if (latestTransaction.get().getTimestamp().isAfter(transaction.getTimestamp())) {
                throw new IllegalArgumentException("Can not store transaction when more recent exists");
            }
        }
    }
}
