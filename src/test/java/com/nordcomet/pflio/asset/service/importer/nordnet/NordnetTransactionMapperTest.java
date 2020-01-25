package com.nordcomet.pflio.asset.service.importer.nordnet;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NordnetTransactionMapperTest {

    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final NordnetTransactionMapper underTest = new NordnetTransactionMapper(assetRepo);

    @Test
    void toTransactionSaveRequest() {
        NordnetTransaction nordnetTransaction = NordnetTransaction.builder()
                .isin("isin-1")
                .currency("CAD")
                .purchasePriceInCurrency(new BigDecimal("9.975625"))
                .purchasePriceInEur(new BigDecimal("6.819848995"))
                .quantity(new BigDecimal("80"))
                .purchaseValueInEur(new BigDecimal("545.5879196"))
                .date(LocalDate.of(2019, 12, 31))
                .build();
        when(assetRepo.findAssetByIsin(nordnetTransaction.getIsin())).thenReturn(Optional.of(Asset.builder().id(1).build()));

        TransactionSaveRequest result = underTest.toTransactionSaveRequest(nordnetTransaction);

        assertEquals(nordnetTransaction.getPurchasePriceInCurrency(), result.getUnitPrice().getAmount());
        assertEquals(Currency.CAD, result.getUnitPrice().getCurrency());
        assertEquals(nordnetTransaction.getQuantity(), result.getQuantityChange());
        assertEquals(nordnetTransaction.getDate().atStartOfDay(), result.getTimestamp());
        assertEquals(nordnetTransaction.getPurchaseValueInEur(), result.getTotalAmount().getAmount());
        assertEquals(new BigDecimal("0.683651299543"), result.getExchangeRate());
    }
}