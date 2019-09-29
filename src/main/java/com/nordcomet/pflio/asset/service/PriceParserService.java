package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.AssetPrice;
import com.nordcomet.pflio.asset.model.ParserOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.nordcomet.pflio.asset.model.ParserType.MORNINGSTAR_FI;

@Service
public class PriceParserService {

    private static final Logger logger = LoggerFactory.getLogger(PriceParserService.class);

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public PriceParserService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public Optional<AssetPrice> getPrice(ParserOptions parserOptions) {
        logger.info("Price parser invoked for {}", parserOptions);

        if (MORNINGSTAR_FI.equals(parserOptions.getParserType())) {
            return getPage(parserOptions.getCode())
                    .flatMap(document -> parsePriceText(document)
                    .flatMap(priceText -> parsePrice(priceText)
                    .flatMap(price -> toEurPrice(price, parserOptions.getSourceCurrency())
                    .map(eurPrice -> new AssetPrice(eurPrice, parserOptions.getTargetCurrency())))));
        }

        logger.info("Price update not implemented for {}", parserOptions);
        return Optional.empty();
    }

    private Optional<BigDecimal> toEurPrice(BigDecimal price, String sourceCurrency) {
        return exchangeRateService.getRateInEURFor(sourceCurrency)
                .map(rate -> price.divide(rate, 4, RoundingMode.HALF_UP));
    }

    private Optional<BigDecimal> parsePrice(String priceText) {
        try {
            String[] split = priceText.split("\\s");
            return Optional.of(new BigDecimal(split[1].replace(",", ".")));
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

    private Optional<Document> getPage(String code) {
        try {
            return Optional.of(Jsoup.connect("http://www.morningstar.fi/fi/funds/snapshot/snapshot.aspx?id=" + code).get());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
