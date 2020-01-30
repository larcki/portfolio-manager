package com.nordcomet.portfolio.service.priceupdate.imports;

import com.nordcomet.portfolio.service.priceupdate.imports.parser.ExchangeRateService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    @ParameterizedTest
    @CsvSource({
            "NOK, EUR",
            "CAD, EUR",
            "GBP, EUR",
            "SEK, EUR",
            "DKK, EUR",
            "GBX, GBP",
    })
    void shouldConvert(String sourceCurrency, String targetCurrency) {
        Optional<BigDecimal> result = exchangeRateService.convert(new BigDecimal("10"), sourceCurrency, targetCurrency);
        assertTrue(result.isPresent());
        System.out.println(result.get());
    }

}