package com.nordcomet.pflio.asset.service.importer.fidelity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@Slf4j
public class FidelityTransactionReader {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public List<FidelityTransaction> readTransactions(File file) {
        try {
            return Files.lines(file.toPath())
                    .skip(8)
                    .map(this::toTransaction)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to read file {}", file, e);
            return emptyList();
        }
    }

    private FidelityTransaction toTransaction(String row) {
        String[] values = row.split(",");

        return FidelityTransaction.builder()
                .orderDate(toLocalDate(values[0]))
                .investment(values[3])
                .amount(toBigDecimal(values[7]))
                .quantity(toBigDecimal(values[8]))
                .pricePerUnit(toBigDecimal(values[9]))
                .build();
    }

    private BigDecimal toBigDecimal(String value) {
        return new BigDecimal(value.replace(",", ""));
    }

    private LocalDate toLocalDate(String value) {
        return LocalDate.parse(value, formatter);
    }

}
