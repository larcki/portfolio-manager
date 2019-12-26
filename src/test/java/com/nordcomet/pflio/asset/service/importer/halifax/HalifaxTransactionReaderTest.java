package com.nordcomet.pflio.asset.service.importer.halifax;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class HalifaxTransactionReaderTest {

    private final HalifaxTransactionReader underTest = new HalifaxTransactionReader();

    @Test
    @Disabled
    void shouldImportHalifaxTransactions() {
        String filename = getClass().getClassLoader().getResource("halifax-transactions.html").getFile();

        List<HalifaxTransaction> result = underTest.readTransactions(new File(filename));

        result.forEach(System.out::println);
    }
}