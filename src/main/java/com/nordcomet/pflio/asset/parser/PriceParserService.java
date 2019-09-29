package com.nordcomet.pflio.asset.parser;

import com.nordcomet.pflio.asset.model.AssetPrice;
import com.nordcomet.pflio.asset.model.ParserOptions;
import com.nordcomet.pflio.asset.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.nordcomet.pflio.asset.model.ParserType.MORNINGSTAR_FUND;
import static com.nordcomet.pflio.asset.model.ParserType.MORNINGSTAR_STOCK;

@Service
public class PriceParserService {

    private static final Logger logger = LoggerFactory.getLogger(PriceParserService.class);

    private final MorningstarStockParser morningstarStockParser;
    private final MorningstarFundParser morningstarFundParser;

    @Autowired
    public PriceParserService(MorningstarStockParser morningstarStockParser, MorningstarFundParser morningstarFundParser) {
        this.morningstarStockParser = morningstarStockParser;
        this.morningstarFundParser = morningstarFundParser;
    }

    public Optional<AssetPrice> getPrice(ParserOptions parserOptions) {
        logger.info("Price parser invoked for {}", parserOptions);

        if (MORNINGSTAR_FUND.equals(parserOptions.getParserType())) {
            return morningstarFundParser.parsePrice(parserOptions);
        }
        if (MORNINGSTAR_STOCK.equals(parserOptions.getParserType())) {
            return morningstarStockParser.parsePrice(parserOptions);
        }

        logger.info("Price update not implemented for {}", parserOptions);
        return Optional.empty();
    }

}
