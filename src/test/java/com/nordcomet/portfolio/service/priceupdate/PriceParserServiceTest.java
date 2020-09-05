package com.nordcomet.portfolio.service.priceupdate;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.ParserOptions;
import com.nordcomet.portfolio.service.priceupdate.parser.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.nordcomet.portfolio.service.priceupdate.parser.ParserType.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceParserServiceTest {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final PageDocumentProvider pageDocumentProvider = new PageDocumentProvider();
    private final MorningstarStockParser morningstarStockParser = new MorningstarStockParser(exchangeRateService, pageDocumentProvider);
    private final MorningstarFundParser morningstarFundParser = new MorningstarFundParser(exchangeRateService, pageDocumentProvider);
    private final MorningstarFundUkParser morningstarFundUkParser = new MorningstarFundUkParser(exchangeRateService, pageDocumentProvider);

    private final PriceParserService priceParserService = new PriceParserService(morningstarStockParser, morningstarFundParser, morningstarFundUkParser);

    @Test
    void getPrice_shouldParsePriceFromMorningstarFi() {
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND, "F00000TH8U", "NOK", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND, "F00000TH8W", "EUR", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_STOCK, "0P00009VJH", "CAD", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND_UK, "F000003VEC", "GBP", "GBP"));
    }

    private void assertParserFor(ParserOptions options) {
        Optional<Money> price = priceParserService.getPrice(options);
        assertTrue(price.isPresent());
        System.out.println(price.get());
    }

}