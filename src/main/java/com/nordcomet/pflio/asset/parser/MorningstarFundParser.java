package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.model.AssetPrice;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.service.ExchangeRateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Optional.of;

@Service
public class MorningstarFundParser {

    private final ExchangeRateService exchangeRateService;

    public MorningstarFundParser(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public Optional<AssetPrice> parsePrice(ParserOptions parserOptions) {
        return getPage(parserOptions.getCode())
                .flatMap(document -> parsePriceText(document)
                .flatMap(priceText -> parsePrice(priceText)
                .flatMap(price -> convertCurrency(price, parserOptions)
                .map(eurPrice -> new AssetPrice(eurPrice, parserOptions.getTargetCurrency())))));
    }

    private Optional<BigDecimal> convertCurrency(BigDecimal price, ParserOptions parserOptions) {
        return exchangeRateService.convert(price, parserOptions.getSourceCurrency(), parserOptions.getTargetCurrency());
    }

    private Optional<BigDecimal> parsePrice(String priceText) {
        try {
            String[] split = priceText.split("\\s");
            return of(new BigDecimal(split[1].replace(",", ".")));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<String> parsePriceText(Document page) {
        try {
            return Optional.ofNullable(page.select("#overviewQuickstatsDiv table tr").get(1).select("td").last().text());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    protected Optional<Document> getPage(String code) {
        try {
            return of(Jsoup.connect("http://www.morningstar.fi/fi/funds/snapshot/snapshot.aspx?id=" + code).get());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
