package com.nordcomet.portfolio.data.asset;

import com.nordcomet.portfolio.service.priceupdate.parser.ParserType;

import javax.persistence.Embeddable;

@Embeddable
public class ParserOptions {

    private ParserType parserType;
    private String code;
    private String sourceCurrency;
    private String targetCurrency;

    public ParserOptions(ParserType parserType, String code, String sourceCurrency, String targetCurrency) {
        this.parserType = parserType;
        this.code = code;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    public ParserOptions() {
    }

    public ParserType getParserType() {
        return parserType;
    }

    public void setParserType(ParserType parserType) {
        this.parserType = parserType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @Override
    public String toString() {
        return "ParserOptions{" +
                "parserType=" + parserType +
                ", code='" + code + '\'' +
                ", sourceCurrency='" + sourceCurrency + '\'' +
                ", targetCurrency='" + targetCurrency + '\'' +
                '}';
    }
}
