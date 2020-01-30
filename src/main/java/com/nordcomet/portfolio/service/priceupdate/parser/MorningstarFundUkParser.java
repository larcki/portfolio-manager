package com.nordcomet.portfolio.service.priceupdate.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MorningstarFundUkParser extends MorningstarFundParser {

    @Autowired
    public MorningstarFundUkParser(ExchangeRateService exchangeRateService,
                                   PageDocumentProvider pageDocumentProvider) {
        super(exchangeRateService, pageDocumentProvider);
    }

    @Override
    protected String getUrl(String code) {
        return "http://www.morningstar.co.uk/uk/funds/snapshot/snapshot.aspx?id=" + code;
    }

}
