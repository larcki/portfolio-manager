package com.nordcomet.portfolio.service.transaction.imports.nordnet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NordnetTransactionReaderTest {

    private final NordnetTransactionReader underTest = new NordnetTransactionReader();

    @Test
    @Disabled
    void shouldImportNordnetTransactions() {
        String filename = getClass().getClassLoader().getResource("PortfolioReport.xls").getFile();

        List<NordnetTransaction> nordnetTransactions = underTest.readTransactions(new File(filename));

        assertEquals(207, nordnetTransactions.size());
        nordnetTransactions.forEach(nordnetTransaction -> {
            assertNotNull(nordnetTransaction.getAssetName());
            assertNotNull(nordnetTransaction.getIsin());
            assertNotNull(nordnetTransaction.getDate());
            assertNotNull(nordnetTransaction.getPurchasePriceInCurrency());
            assertNotNull(nordnetTransaction.getPurchasePriceInEur());
            assertNotNull(nordnetTransaction.getPurchaseValueInCurrency());
            assertNotNull(nordnetTransaction.getPurchaseValueInEur());
            assertNotNull(nordnetTransaction.getMarketValueInCurrency());
            assertNotNull(nordnetTransaction.getMarketValueInEur());
            assertNotNull(nordnetTransaction.getCurrency());
            assertNotNull(nordnetTransaction.getQuantity());
        });
    }
}