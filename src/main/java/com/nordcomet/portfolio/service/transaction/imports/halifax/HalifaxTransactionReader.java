package com.nordcomet.portfolio.service.transaction.imports.halifax;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class HalifaxTransactionReader {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    List<HalifaxTransaction> readTransactions(File file) {
        try {
            return Jsoup.parse(Files.readString(file.toPath()))
                    .getElementById("divDealingHistDT")
                    .getElementsByTag("tr")
                    .stream()
                    .skip(1)
                    .map(element -> {
                        Elements row = element.getElementsByTag("td");
                        return toHalifaxTransaction(row);
                    })
                    .sorted(comparing(HalifaxTransaction::getDate))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HalifaxTransaction toHalifaxTransaction(Elements row) {
        return HalifaxTransaction.builder()
                .date(toLocalDate(row.get(0)))
                .companyCode(row.get(2).text())
                .quantity(toBigDecimal(row.get(4)))
                .executedPrice(toBigDecimal(row.get(5)))
                .netConsideration(toBigDecimal(row.get(6)))
                .reference(row.get(7).text())
                .build();
    }

    private BigDecimal toBigDecimal(Element element) {
        return new BigDecimal(element.text().replace(",", ""));
    }


    private LocalDate toLocalDate(Element element) {
        return LocalDate.parse(element.text(), formatter);
    }

}
