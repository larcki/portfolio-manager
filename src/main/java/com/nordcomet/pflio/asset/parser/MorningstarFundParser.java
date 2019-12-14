package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.model.Currency;
import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@Slf4j
public class MorningstarFundParser {

    private final ExchangeRateService exchangeRateService;
    private final PageDocumentProvider pageDocumentProvider;

    @Autowired
    public MorningstarFundParser(ExchangeRateService exchangeRateService,
                                 PageDocumentProvider pageDocumentProvider) {
        this.exchangeRateService = exchangeRateService;
        this.pageDocumentProvider = pageDocumentProvider;
    }

    public Optional<Money> parsePrice(ParserOptions parserOptions) {
        return pageDocumentProvider.getPage(getUrl(parserOptions.getCode()))
                .flatMap(document -> parsePriceText(document)
                .flatMap(priceText -> parsePrice(priceText)
                .flatMap(price -> convertCurrency(price, parserOptions)
                .map(eurPrice -> Money.of(eurPrice, Currency.toCurrency(parserOptions.getTargetCurrency()))))));
    }

    protected String getUrl(String code) {
        return "http://www.morningstar.fi/fi/funds/snapshot/snapshot.aspx?id=" + code;
    }

    private Optional<BigDecimal> convertCurrency(BigDecimal price, ParserOptions parserOptions) {
        return exchangeRateService.convert(price, parserOptions.getSourceCurrency(), parserOptions.getTargetCurrency());
    }

    private Optional<BigDecimal> parsePrice(String priceText) {
        try {
            String[] split = priceText.split("\\s");
            return of(new BigDecimal(split[1].replace(",", ".")));
        } catch (Exception e) {
            log.error("Count not parse price", e);
            return Optional.empty();
        }
    }

    private Optional<String> parsePriceText(Document page) {
        try {
            return Optional.ofNullable(page.select("#overviewQuickstatsDiv table tr").get(1).select("td").last().text());
        } catch (Exception e) {
            log.error("Count not parse price test", e);
            return Optional.empty();
        }
    }

}
