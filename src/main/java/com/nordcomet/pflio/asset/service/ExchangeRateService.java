package com.nordcomet.pflio.asset.service;

import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    public Optional<BigDecimal> convert(BigDecimal price, String sourceCurrency, String targetCurrency) {

        if (sourceCurrency.equals(targetCurrency)) {
            return of(price);
        }
        if ("EUR".equals(targetCurrency)) {
            return toEurPrice(price, sourceCurrency);
        }
        if ("GBX".equals(sourceCurrency) && "GBP".equals(targetCurrency)) {
            return of(price.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        }

        logger.error("Target currency not supported: {}", sourceCurrency);
        return empty();
    }

    private Optional<BigDecimal> toEurPrice(BigDecimal price, String sourceCurrency) {
        return getRateInEURFor(sourceCurrency).map(rate -> price.divide(rate, 4, RoundingMode.HALF_UP));
    }

    private Optional<BigDecimal> getRateInEURFor(String sourceCurrency) {
        try {
            return of(Unirest.get("https://api.exchangeratesapi.io/latest")
                    .queryString("symbols", sourceCurrency)
                    .asJson()
                    .getBody()
                    .getObject()
                    .getJSONObject("rates")
                    .getBigDecimal(sourceCurrency));
        } catch (Exception e) {
            e.printStackTrace();
            return empty();
        }
    }

}
