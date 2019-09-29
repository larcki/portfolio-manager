package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.AssetPrice;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.model.ParserType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceParserServiceTest {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    private final PriceParserService priceParserService = new PriceParserService(exchangeRateService);

    @Test
    public void getPrice_shouldParsePriceFromMorningstarFi() {
        ParserOptions parserOptions = new ParserOptions();
        parserOptions.setCode("F00000TH8U");
        parserOptions.setSourceCurrency("NOK");
        parserOptions.setParserType(ParserType.MORNINGSTAR_FI);

        Optional<AssetPrice> price = priceParserService.getPrice(parserOptions);

        assertTrue(price.isPresent());
        System.out.println(price.get());

    }

}