package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.model.Money;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.service.ExchangeRateService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.nordcomet.pflio.asset.model.ParserType.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceParserServiceTest {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final MorningstarStockParser morningstarStockParser = new MorningstarStockParser(exchangeRateService);
    private final MorningstarFundParser morningstarFundParser = new MorningstarFundParser(exchangeRateService);
    private final MorningstarFundUkParser morningstarFundUkParser = new MorningstarFundUkParser(exchangeRateService);

    private final PriceParserService priceParserService = new PriceParserService(morningstarStockParser, morningstarFundParser, morningstarFundUkParser);

    @Test
    public void getPrice_shouldParsePriceFromMorningstarFi() {
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND, "F00000TH8U", "NOK", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND, "F00000TH8W", "EUR", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_STOCK, "0P00009VJH", "CAD", "EUR"));
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND_UK, "F00000OXGD", "GBX", "GBP"));
        assertParserFor(new ParserOptions(MORNINGSTAR_FUND_UK, "F000003VEC", "GBP", "GBP"));
    }

    private void assertParserFor(ParserOptions options) {
        Optional<Money> price = priceParserService.getPrice(options);
        assertTrue(price.isPresent());
        System.out.println(price.get());
    }

}