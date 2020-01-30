package com.nordcomet.portfolio.service.priceupdate.imports.parser;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.ParserOptions;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class MorningstarStockParser {

    private final ExchangeRateService exchangeRateService;
    private final PageDocumentProvider pageDocumentProvider;

    @Autowired
    public MorningstarStockParser(ExchangeRateService exchangeRateService, PageDocumentProvider pageDocumentProvider) {
        this.exchangeRateService = exchangeRateService;
        this.pageDocumentProvider = pageDocumentProvider;
    }

    public Optional<Money> parsePrice(ParserOptions parserOptions) {
        return getPage(parserOptions.getCode())
                .flatMap(document -> parsePriceText(document)
                .flatMap(priceText -> parsePrice(priceText)
                .flatMap(price -> convertCurrency(price, parserOptions)
                .map(convertedPrice -> Money.of(convertedPrice, Currency.toCurrency(parserOptions.getTargetCurrency()))))));
    }

    private Optional<BigDecimal> convertCurrency(BigDecimal price, ParserOptions parserOptions) {
        return exchangeRateService.convert(price, parserOptions.getSourceCurrency(), parserOptions.getTargetCurrency());
    }

    private Optional<BigDecimal> parsePrice(String priceText) {
        return Optional.of(new BigDecimal(priceText.replace(",", ".")));
    }

    private Optional<String> parsePriceText(Document page) {
        try {
            return Optional.ofNullable(page.select("#Col0Price").text());
        } catch (Exception e) {
            log.error("Could not parse price text", e);
            return Optional.empty();
        }
    }

    private Optional<Document> getPage(String code) {
        return pageDocumentProvider.getPage("http://tools.morningstar.fi/fi/stockreport/default.aspx?Site=fi&id=" + code);
    }

}
