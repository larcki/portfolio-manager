package com.nordcomet.portfolio.service.priceupdate;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.ParserOptions;
import com.nordcomet.portfolio.service.priceupdate.parser.MorningstarFundParser;
import com.nordcomet.portfolio.service.priceupdate.parser.MorningstarFundUkParser;
import com.nordcomet.portfolio.service.priceupdate.parser.MorningstarStockParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.nordcomet.portfolio.service.priceupdate.parser.ParserType.*;

@Service
public class PriceParserService {

    private static final Logger logger = LoggerFactory.getLogger(PriceParserService.class);

    private final MorningstarStockParser morningstarStockParser;
    private final MorningstarFundParser morningstarFundParser;
    private final MorningstarFundUkParser morningstarFundUkParser;

    @Autowired
    public PriceParserService(MorningstarStockParser morningstarStockParser, MorningstarFundParser morningstarFundParser, MorningstarFundUkParser morningstarFundUkParser) {
        this.morningstarStockParser = morningstarStockParser;
        this.morningstarFundParser = morningstarFundParser;
        this.morningstarFundUkParser = morningstarFundUkParser;
    }

    public Optional<Money> getPrice(ParserOptions parserOptions) {
        logger.info("Price parser invoked for {}", parserOptions);

        if (MORNINGSTAR_FUND.equals(parserOptions.getParserType())) {
            return morningstarFundParser.parsePrice(parserOptions);
        }
        if (MORNINGSTAR_FUND_UK.equals(parserOptions.getParserType())) {
            return morningstarFundUkParser.parsePrice(parserOptions);
        }
        if (MORNINGSTAR_STOCK.equals(parserOptions.getParserType())) {
            return morningstarStockParser.parsePrice(parserOptions);
        }

        logger.info("Price update not implemented for {}", parserOptions);
        return Optional.empty();
    }

}
