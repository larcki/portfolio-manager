package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.service.ExchangeRateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.of;

@Service
public class MorningstarFundUkParser extends MorningstarFundParser {

    public MorningstarFundUkParser(ExchangeRateService exchangeRateService) {
        super(exchangeRateService);
    }

    @Override
    protected Optional<Document> getPage(String code) {
        try {
            return of(Jsoup.connect("http://www.morningstar.co.uk/uk/funds/snapshot/snapshot.aspx?id=" + code).get());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
