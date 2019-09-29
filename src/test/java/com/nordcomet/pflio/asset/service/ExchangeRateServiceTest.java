package com.nordcomet.pflio.asset.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Test
    void shouldGetExchangeRate() {
        Optional<BigDecimal> result = exchangeRateService.convert(new BigDecimal("10"), "NOK", "EUR");
        assertTrue(result.isPresent());
    }
}