package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.service.ExchangeRateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Optional.of;

@Service
public class MorningstarStockParser {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public MorningstarStockParser(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
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
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<Document> getPage(String code) {
        try {
            return of(Jsoup.connect("http://tools.morningstar.fi/fi/stockreport/default.aspx?Site=fi&id=" + code).get());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
