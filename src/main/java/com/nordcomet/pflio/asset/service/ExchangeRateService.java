package com.nordcomet.pflio.asset.service;

import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Optional.*;

@Service
public class ExchangeRateService {

    public Optional<BigDecimal> getRateInEURFor(String sourceCurrency) {
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
